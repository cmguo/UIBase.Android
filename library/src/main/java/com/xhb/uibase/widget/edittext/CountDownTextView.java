package com.xhb.uibase.widget.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.xhb.uibase.R;


public class CountDownTextView extends AppCompatTextView implements View.OnClickListener {

    private String mPreText;

    private String mAfterText;

    private int mTime;

    private String mUnit;

    private CountTimer mCountTimer;

    private OnCountDownClickedListener onCountDownClickedListener;

    private int mDisableColor = ResourcesCompat.getColor(getResources(), R.color.static_bluegrey_800, null);
    private int mEnableColor = ResourcesCompat.getColor(getResources(), R.color.static_bluegrey_800, null);

    public CountDownTextView(Context context) {
        this(context, null);
    }

    public CountDownTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CountDownTextView);
        mPreText = array.getString(R.styleable.CountDownTextView_preText);
        if (TextUtils.isEmpty(mPreText)) {
            mPreText = getResources().getString(R.string.count_down_text_view_default_pre_text);
        }
        mAfterText = array.getString(R.styleable.CountDownTextView_afterText);
        if (TextUtils.isEmpty(mAfterText)) {
            mAfterText = getResources().getString(R.string.count_down_text_view_default_after_text);
        }
        mUnit = array.getString(R.styleable.CountDownTextView_unit);
        if (TextUtils.isEmpty(mUnit)) {
            mUnit = "s";
        }
        mDisableColor = array.getColor(R.styleable.CountDownTextView_disableColor, mDisableColor);
        mEnableColor = array.getColor(R.styleable.CountDownTextView_enableColor, mEnableColor);
        mTime = array.getInteger(R.styleable.CountDownTextView_totalTime, 60);
        array.recycle();
        setText(mPreText);
        setTextColor(mEnableColor);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        if (mCountTimer == null) {
            mCountTimer = new CountTimer(mTime * 1000, 1000);
        }
        mCountTimer.start();
        setEnabled(false);
        setTextColor(mDisableColor);
        if (onCountDownClickedListener != null) {
            onCountDownClickedListener.clicked();
        }
    }

    public void setPreText(String mPreText) {
        this.mPreText = mPreText;
        setText(mPreText);
    }

    public void setAfterText(String mAfterText) {
        this.mAfterText = mAfterText;
    }

    public void setTime(int mTime) {
        this.mTime = mTime;
    }

    public void setDisableColor(@ColorInt int mDisableColor) {
        this.mDisableColor = mDisableColor;
    }

    public void setEnableColor(@ColorInt int mEnableColor) {
        this.mEnableColor = mEnableColor;
        setTextColor(mEnableColor);
    }

    class CountTimer extends CountDownTimer {
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long lastTime = millisUntilFinished / 1000;
            setText(String.format(getResources().getString(R.string.count_down_text_view_time), lastTime, mUnit));
        }

        @Override
        public void onFinish() {
            setText(mAfterText);
            setEnabled(true);
            setTextColor(mEnableColor);
        }
    }

    public interface OnCountDownClickedListener {
        void clicked();
    }

    public void setOnCountDownClickedListener(OnCountDownClickedListener onCountDownClickedListener) {
        this.onCountDownClickedListener = onCountDownClickedListener;
    }
}
