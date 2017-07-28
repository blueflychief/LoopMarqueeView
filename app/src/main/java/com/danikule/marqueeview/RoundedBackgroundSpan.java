package com.danikule.marqueeview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ReplacementSpan;

/**
 * Created by lsq on 7/28/2017.
 */

public class RoundedBackgroundSpan extends ReplacementSpan {
    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, @IntRange(from = 0) int start, @IntRange(from = 0) int end, @Nullable Paint.FontMetricsInt fm) {
        return 0;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, @IntRange(from = 0) int start, @IntRange(from = 0) int end, float x, int top, int y, int bottom, @NonNull Paint paint) {

    }

//    @Override
//    public void draw(@NonNull Canvas canvas, CharSequence text, @IntRange(from = 0) int start, @IntRange(from = 0) int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
//        RectF rect = new RectF(x, top, x + MeasureText(paint, text, start, end), bottom);
//        paint.getClass() = Application.Context.Resources.GetColor(Resource.Color.nextTimeBackgroundColor);
//        canvas.DrawRoundRect(rect, Application.Context.Resources.GetDimensionPixelSize(Resource.Dimension.localRouteDetailsRoundRectValue), Application.Context.Resources.GetDimensionPixelSize(Resource.Dimension.localRouteDetailsRoundRectValue), paint);
//        paint.getColor() = Application.Context.Resources.GetColor(Resource.Color.nextTimeTextColor);
//        canvas.drawText(text, start, end, x, y, paint);
//    }
//
//
//
//    int GetSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
//        return Math.round(MeasureText(paint, text, start, end));
//    }
//
//    private float MeasureText(Paint paint, CharSequence text, int start, int end) {
//        return paint.measureText(text, start, end);
//    }
}
