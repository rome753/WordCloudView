package com.example.chao.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * Created by chao on 2019/2/15.
 */

public class WordCloudView extends FrameLayout implements View.OnClickListener {

    Random random = new Random();
    String[] words;
    HashSet<View> placed = new HashSet<>();

    public WordCloudView(@NonNull Context context) {
        this(context, null);
    }

    public WordCloudView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WordCloudView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

            List<Point> spiral = generateSpiral();
            for(Point p : spiral) {
                pivotX += p.x;
                pivotY += p.y;

                Log.d("chao", "place " + pivotX + "," + pivotY);
                Rect r1 = getVisualRect(pivotX, pivotY, w, h, v.getRotation());
                boolean isOverlap = false;
                for(View pv : placed) {
                    Rect r2 = getVisualRect(pv);
                    if(isOverlap(r1, r2)) {
                        isOverlap = true;
                        break;
                    }
                }
                if(isOverlap) {

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

    public Rect getVisualRect(int pivotX, int pivotY, int width, int height, float rotation) {
        if(rotation != 0) {
            int temp = width;
            width = height;
            height = temp;
        }
        return getRect(pivotX, pivotY, width, height);
    }

    public Rect getVisualRect(View v) {
        return getVisualRect(
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

    //设置Cloud的颜色
    private final int[] colors = new int[] {
            Color.rgb(36,252,3),
            Color.rgb(187,134,252),
            Color.rgb(187,134,252),
    };

    public void addTextView(String word, int weight) {
        TextView tv = new TextView(getContext());
        tv.setText(word);
        tv.setTextSize(weight);
        tv.setTextColor(colors[weight/20]);
        tv.setRotation(rotates[random.nextInt(rotates.length)]);
        tv.setOnClickListener(this);
        addView(tv, params);
    }

    TextView lastText;

    @Override
    public void onClick(View v) {
        if(v instanceof TextView) {
            Log.e("chao", "click " + ((TextView) v).getText() + " size:\t" + String.valueOf( ((TextView) v ).getTextSize()));
            ((TextView) v).setTextColor(Color.RED);
            if(lastText != null) {
                lastText.setTextColor(Color.BLACK);
//                lastText.setTextColor(colors[(int) (((TextView) v ).getTextSize() / 25)]);
            }
            lastText = (TextView) v;
        }
    }

    private List<Point> generateSpiral() {
        List<Point> res = new ArrayList<>();
        int A = 10;
        int w = 5;
        double sita = Math.PI;
        for(double t = 0; t < 10 * Math.PI; t+=0.1) {
            int x = Double.valueOf(A * Math.cos(w * t + sita)).intValue();
            int y = Double.valueOf(A * Math.sin(w * t + sita)).intValue();
            A += 1;
            res.add(new Point(x, y));
            Log.e("chao", x + ", " + y);
        }
        return res;
    }
}
