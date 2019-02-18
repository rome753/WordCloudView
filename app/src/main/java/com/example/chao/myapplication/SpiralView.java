package com.example.chao.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chao on 2019/2/17.
 */

public class SpiralView extends View {

    Paint paint;
    List<Point> points;

    public SpiralView(Context context) {
        this(context, null);
    }

    public SpiralView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpiralView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);

        points = generateSpiral();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        for(Point p : points) {
            canvas.drawCircle(cx + p.x, cy + p.y, 5, paint);
        }
    }

    private List<Point> generateSpiral() {
        List<Point> res = new ArrayList<>();
        int A = 10;
        int w = 1;
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
