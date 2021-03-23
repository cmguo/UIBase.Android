package com.xhb.uibase.widget.edittext;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

public class ProgressButton extends AppCompatButton implements Animatable {
    private ProgressDrawable drawable;

    public interface onAnimFinish {
        void onFinish();
    }

    private onAnimFinish listener;

    public ProgressButton(Context context) {
        super(context);
        init();
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        drawable = new ProgressDrawable(getTextSize(), this);
        drawable.setColorDefault(getCurrentTextColor());
        drawable.setAnimatable(this);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    public void startRotate() {
        this.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        setCompoundDrawablePadding(20);
        drawable.startRotate();
    }

    public void animFinish() {
        drawable.animFinish();
    }

    public void animError() {
        drawable.animError();
    }

    public void removeDrawable() {
        this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        drawable.stopRotate();
    }

    @Override
    public void start() {
        startRotate();
    }

    @Override
    public void stop() {
        if (listener != null) {
            listener.onFinish();
        }
    }

    public void setOnAnimFinishListener(onAnimFinish listener) {
        this.listener = listener;
    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
