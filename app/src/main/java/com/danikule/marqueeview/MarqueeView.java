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
import android.util.Log;
import android.view.View;

/**
 * 循环跑马灯
 */

public class MarqueeView extends View {
    private static final String TAG = MarqueeView.class.getSimpleName();
    private Paint textPaint;
    private Rect textBound;
    private String textString = "这是";
    private volatile boolean isPlaying = false;
    private int viewWidth;
    private int viewHeight;
    private int intervalOffset = 3;//每次移动的偏移量
    private int intervalTime = 16;  //移动时间间隔，单位毫秒
    private int textSpace = 200;//两个移动空间间隔
    private int currentOffset = textSpace;//当前所处的偏移量
    private boolean awaysLoop = true;//不管字数多少，都滚动
    private boolean textInCenter = true;//字数不足一行时文字居中

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
        int viewWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int textWidth = textBound.width();
        Log.d(TAG, "onDraw,viewWidth: " + viewWidth + ",textWidth:" + textWidth + ",currentOffset:" + currentOffset);
        boolean canLoop = textWidth > viewWidth || awaysLoop;
        if (canLoop) {
            canvas.drawText(textString, -textBound.left + currentOffset, -textBound.top, textPaint);
            int count = viewWidth / (textWidth + textSpace) + 1;
            for (int i = 0; i < count; i++) {
                canvas.drawText(textString,
                        -textBound.left + currentOffset + (textBound.width() + textSpace) * (1 + i),
                        -textBound.top, textPaint);
            }
            if (isPlaying) {
                postDelayed(offsetRunnable, intervalTime);
            }
        } else {
            canvas.drawText(textString, textInCenter ? (viewWidth - textWidth) * 0.5f : 0, -textBound.top, textPaint);
        }
    }

    private Runnable offsetRunnable = new Runnable() {
        @Override
        public void run() {
            currentOffset -= intervalOffset;
            postInvalidate();
            if (currentOffset < -textBound.width()) {
                currentOffset = (textSpace - intervalOffset);
            }
        }
    };

    public void setText(String text) {
        setText(text, true);
    }

    public void setText(String text, boolean autoStart) {
        stop();
        textString = text;
        currentOffset = textSpace;
        isPlaying = autoStart;
        textPaint.getTextBounds(textString, 0, textString.length(), textBound);
        if (autoStart) {
            start();
        } else {
            postInvalidate();
        }
    }

    public void start() {
        isPlaying = true;
        postInvalidate();
    }

    public MarqueeView setTextInCenter(boolean textInCenter) {
        this.textInCenter = textInCenter;
        return this;
    }

    public MarqueeView setIntervalOffset(int intervalOffset) {
        this.intervalOffset = intervalOffset;
        return this;
    }

    public MarqueeView setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
        return this;
    }

    public MarqueeView setTextSpace(int textSpace) {
        this.textSpace = textSpace;
        return this;
    }

    public MarqueeView setAwaysLoop(boolean awaysLoop) {
        this.awaysLoop = awaysLoop;
        return this;
    }

    public void stop() {
        isPlaying = false;
        if (offsetRunnable != null) {
            removeCallbacks(offsetRunnable);
        }
    }

    public void release() {
        stop();
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
            textSpace = bundle.getInt("textSpace");
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
        bundle.putInt("textSpace", textSpace);
        stop();
        return bundle;
    }
}
