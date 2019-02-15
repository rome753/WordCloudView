package com.example.chao.myapplication;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Random;

/**
 * Created by chao on 2019/2/15.
 */

public class WordCloudView extends FrameLayout implements View.OnClickListener {

    Random random = new Random();
    String[] words;
    HashSet<View> placed = new HashSet<>();
    int weight = 20;
    int off = 2;

    public WordCloudView(@NonNull Context context) {
        this(context, null);
    }

    public WordCloudView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WordCloudView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String s = String.valueOf(System.currentTimeMillis());
                s = s.substring(random.nextInt(11));
                addTextView(s, weight);
                if(--off == 0) {
                    off = 3;
                    if(weight > 5) weight--;
                }
                sendEmptyMessageDelayed(0, 100);
            }
        };
        handler.sendEmptyMessage(0);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int n = getChildCount();
        for(int i = 0; i < n; i++) {
            View v = getChildAt(i);
            if(placed.contains(v)) {
                continue;
            }
            int w = v.getMeasuredWidth();
            int h = v.getMeasuredHeight();

            int pivotX = getWidth() / 3 + random.nextInt(getWidth() / 3);
            int pivotY = getHeight() / 3 + random.nextInt(getHeight() / 3);
            int dir = 0; // 0-left, 1-top, 2-right, 3-bottom
            int len = 2;
            while(pivotX > 0 && pivotX < getWidth() && pivotY > 0 && pivotY < getHeight()) {
                Log.d("chao", "place " + pivotX + "," + pivotY);
                Rect r1 = getVisibleRect(pivotX, pivotY, w, h, v.getRotation());
                boolean isOverlap = false;
                for(View pv : placed) {
                    Rect r2 = getVisibleRect(pv);
                    if(isOverlap(r1, r2)) {
                        isOverlap = true;
                        break;
                    }
                }
                if(isOverlap) {
                    Log.d("chao", "isOverlap " + pivotX + "," + pivotY);
                    switch (dir) {
                        case 0:
                            pivotX -= len;
                            break;
                        case 1:
                            pivotY -= len;
                            len += 2;
                            break;
                        case 2:
                            pivotX += len;
                            break;
                        case 3:
                            pivotY += len;
                            len += 2;
                            break;
                    }
                    dir = ++dir % 4;
                } else {
                    Log.d("chao", "placed");
                    Rect r = getRect(pivotX, pivotY, w, h);
                    v.layout(r.left, r.top, r.right, r.bottom);
                    break;
                }
            }
            placed.add(v);
        }
    }

    public Rect getRect(int pivotX, int pivotY, int width, int height) {
        return new Rect(
                pivotX - width / 2,
                pivotY - height / 2,
                pivotX + width / 2,
                pivotY + height / 2
        );
    }

    public Rect getVisibleRect(int pivotX, int pivotY, int width, int height, float rotation) {
        if(rotation != 0) {
            int temp = width;
            width = height;
            height = temp;
        }
        return getRect(pivotX, pivotY, width, height);
    }

    public Rect getVisibleRect(View v) {
        return getVisibleRect(
                    (v.getRight() + v.getLeft()) / 2,
                    (v.getBottom() + v.getTop()) / 2,
                    v.getMeasuredWidth(),
                    v.getMeasuredHeight(),
                    v.getRotation()
                );
    }

    public static boolean isOverlap(Rect r1, Rect r2) {
        return r1.right >= r2.left && r2.right >= r1.left
                && r1.bottom >= r2.top && r2.bottom >= r1.top;
    }



//    public void setWords(String[] words) {
//        this.words = words;
//        placed.clear();
//        removeAllViews();
//        for(final String word : words) {
//            addTextView(word);
//        }
//    }

    LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    float[] rotates = {
            0f,90f,270f
    };

    private void addTextView(String word, int weight) {
        TextView tv = new TextView(getContext());
        tv.setText(word);
        tv.setTextSize(weight);
        tv.setRotation(rotates[random.nextInt(rotates.length)]);
        tv.setOnClickListener(this);
        addView(tv, params);
    }

    @Override
    public void onClick(View v) {
        if(v instanceof TextView) {
            Log.e("chao", "click " + ((TextView) v).getText());
        }
    }
}