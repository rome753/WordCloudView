package com.example.chao.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chao on 2019/2/17.
 */

public class SpiralView extends View {

    Path path;
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
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setAlpha(10);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setStrokeCap(Paint.Cap.ROUND);

        path = new Path();
        points = generateSpiral();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        path.reset();
        for(int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            int x = cx + p.x;
            int y = cy + p.y;
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        canvas.drawPath(path, paint);
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
