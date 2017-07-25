package com.danikule.marqueeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 循环跑马灯
 */

public class MarqueeView extends View {
    private Paint textPaint;
    private Rect textBound;
    private String textString = "这是跑马灯在赛跑";
    private volatile boolean isPlaying = false;
    private int viewWidth;
    private int viewHeight;
    private int currentOffset = 0;//当前所处的偏移量
    private int intervalOffset = 3;//每次移动的偏移量
    private int intervalTime = 30;  //移动时间间隔，单位毫秒
    private int intervalSpace = 60;//两个移动空间间隔

    public MarqueeView(Context context) {
        super(context);
        init();
    }

    public MarqueeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MarqueeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        textPaint = new Paint();
        textPaint.setColor(Color.RED);
        textPaint.setDither(true);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(60);
        textBound = new Rect();
        textPaint.getTextBounds(textString, 0, textString.length(), textBound);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawText(textString, -textBound.left + currentOffset, -textBound.top, textPaint);
        canvas.drawText(textString, -textBound.left + currentOffset + textBound.width() + intervalSpace, -textBound.top, textPaint);  //这是第二个
//        canvas.drawText(textString, -textBound.left + currentOffset + (textBound.width() + intervalSpace) * 2, -textBound.top, textPaint);  //这是第三个
        if (isPlaying) {
            postDelayed(offsetRunnable, intervalTime);
        }
    }

    private Runnable offsetRunnable = new Runnable() {
        @Override
        public void run() {
            currentOffset -= intervalOffset;
            invalidate();
            if (currentOffset < -textBound.width()) {
                currentOffset = (intervalSpace - intervalOffset);
            }
        }
    };

    public void setText(String text) {
        textString = text;
        currentOffset = 0;
        isPlaying = false;
        textPaint.getTextBounds(textString, 0, textString.length(), textBound);
        invalidate();
    }

    public void start() {
        isPlaying = true;
        invalidate();
    }

    public void stop() {
        isPlaying = false;
    }

    public void release() {
        isPlaying = false;
        if (offsetRunnable != null) {
            removeCallbacks(offsetRunnable);
        }
        offsetRunnable = null;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    protected void onDetachedFromWindow() {
        release();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        viewWidth = measuredDimension(true, widthMeasureSpec) + getPaddingLeft() + getPaddingRight();
        viewHeight = measuredDimension(false, heightMeasureSpec) + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(viewWidth, viewHeight);
    }

    private int measuredDimension(boolean isWidth, int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {//xml写了确定的值，或者是match_parent
            result = size;
        } else {
            result = isWidth ? textBound.width() : textBound.height();  //如果是wrap_content,默认值是100
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        if (bundle != null) {
            super.onRestoreInstanceState(bundle.getParcelable("superData"));
            isPlaying = bundle.getBoolean("isPlaying");
            textString = bundle.getString("textString");
            currentOffset = bundle.getInt("currentOffset");
            intervalOffset = bundle.getInt("intervalOffset");
            intervalSpace = bundle.getInt("intervalSpace");
            if (isPlaying) {
                init();
                start();
            }
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        if (offsetRunnable != null) {
            removeCallbacks(offsetRunnable);
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable("superData", super.onSaveInstanceState());
        bundle.putBoolean("isPlaying", isPlaying);
        bundle.putString("textString", textString);
        bundle.putInt("currentOffset", currentOffset);
        bundle.putInt("intervalOffset", intervalOffset);
        bundle.putInt("intervalSpace", intervalSpace);
        stop();
        return bundle;
    }
}
