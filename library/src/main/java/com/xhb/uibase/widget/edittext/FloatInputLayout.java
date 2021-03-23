package com.xhb.uibase.widget.edittext;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;

import com.xhb.uibase.R;
import com.xhb.uibase.utils.ScreenUtils;

public final class FloatInputLayout extends LinearLayout {

    private static final long ANIMATION_DURATION = 150;

    private EditText mEditText;

    private TextView mLabel;

    private TextView mCounterView;

    private LinearLayout mIndicatorArea;

    private boolean mCounterEnabled;

    private int mMaxCounter;

    private boolean mHintEnabled = true;

    private boolean isShowLabel;
    /**
     * LabelText 字体颜色
     */
    private int mLabelTextColor;

    /**
     * LabelText 字体大小
     */
    private int mLabelTextSize;

    /**
     * CounterView 字体颜色
     */
    private int mCounterTextColor;

    /**
     * CounterView Flow 字体颜色
     */
    private int mCounterFlowTextColor;

    /**
     * CounterView 字体大小
     */
    private int mCounterTextSize;


    private CharSequence mHint;

    private boolean mErrorEnable;

    private String mErrorMessage;

    private int mErrorTextColor;

    private LinearLayout mInputLayout;

    public FloatInputLayout(Context context) {
        this(context, null);
    }

    public FloatInputLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatInputLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOrientation(VERTICAL);
        mInputLayout = new LinearLayout(context);
        mInputLayout.setOrientation(VERTICAL);
        mInputLayout.setAddStatesFromChildren(true);
        LayoutParams lps = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.weight = 1;
        mInputLayout.setLayoutParams(lps);
        mInputLayout.setGravity(Gravity.CENTER_VERTICAL);
        addView(mInputLayout);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FloatInputLayout);
        int textHintPadding = a.getDimensionPixelSize(R.styleable.FloatInputLayout_textHintPadding, ScreenUtils.dp2PxInt(context, 4));
        mLabel = new TextView(context);
        mLabel.setPadding(0, 0, 0, textHintPadding);
        mLabelTextColor = a.getColor(R.styleable.FloatInputLayout_textColorHint, ResourcesCompat.getColor(getResources(), R.color.static_bluegrey_700, null));
        mLabelTextSize = a.getDimensionPixelSize(R.styleable.FloatInputLayout_textSizeHint, 12);
        mLabel.setTextColor(mLabelTextColor);
        mLabel.setTextSize(mLabelTextSize);
        mInputLayout.addView(mLabel, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLabel.setVisibility(GONE);
        mHintEnabled = a.getBoolean(R.styleable.FloatInputLayout_hintEnabled, true);
        setHint(a.getString(R.styleable.FloatInputLayout_hintText));
        mMaxCounter = a.getInteger(R.styleable.FloatInputLayout_maxCounter, -1);
        mCounterFlowTextColor = a.getColor(R.styleable.FloatInputLayout_counterFlowTextColor, ResourcesCompat.getColor(getResources(), R.color.red_600, null));
        mCounterTextColor = a.getColor(R.styleable.FloatInputLayout_counterTextColor2, ResourcesCompat.getColor(getResources(), R.color.static_bluegrey_700, null));
        mCounterTextSize = a.getDimensionPixelSize(R.styleable.FloatInputLayout_counterTextSize, 12);
        boolean counterEnable = a.getBoolean(R.styleable.FloatInputLayout_counterEnabled, false);
        mErrorEnable = a.getBoolean(R.styleable.FloatInputLayout_errorEnable, false);
        mErrorTextColor = a.getColor(R.styleable.FloatInputLayout_floatInputErrorTextColor, ResourcesCompat.getColor(getResources(), R.color.red_600, null));
        mErrorMessage = a.getString(R.styleable.FloatInputLayout_floatInputErrorMessage);
        a.recycle();
        setCounterEnabled(counterEnable);

    }

    @Override
    public final void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof EditText) {
            // Make sure that the EditText is vertically at the bottom, so that it sits on the
            // EditText's underline
            if (mEditText != null) {
                throw new IllegalArgumentException("We already have an EditText, can only have one");
            }
            LayoutParams childParams = new LayoutParams(params);
            childParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            if (!mHintEnabled) {
                childParams.weight = 1;
            }
            mInputLayout.addView(child, childParams);
            // Now use the EditText's LayoutParams as our own and update them to make enough space
            // for the label

            if (TextUtils.isEmpty(mHint)) {
                mHint = ((EditText) child).getHint();
            }
            setEditText((EditText) child);
        } else {
            // Carry on adding the View...
            super.addView(child, index, params);
        }
    }

    private void setEditText(final EditText editText) {
        mEditText = editText;
        mEditText.setHint(mHint);
        mEditText.setHintTextColor(mLabelTextColor);
        updateEditTextStatus();
        mLabel.setText(mHint);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mCounterEnabled) {
                    updateCounter(s.length());
                }
            }
        });
        if (mHintEnabled && mEditText.getText().toString().length() > 0) {
            showLabel();
        }

        mEditText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //这句话说的意思告诉父View我自己的事件我自己处理
                if (canVerticalScroll(mEditText)) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
    }

    /**
     * EditText竖直方向能否够滚动
     *
     * @param editText 须要推断的EditText
     * @return true：能够滚动   false：不能够滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if (scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

    private void updateEditTextStatus() {
        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                mLabel.setActivated(focused);
                updateFloatStatus(focused);
            }
        });
    }

    private void updateFloatStatus(boolean focused) {
        if (mHintEnabled) {
            if (focused) {
                showLabel();
                mLabel.setText(mHint);
                mLabel.setTextColor(mLabelTextColor);
                mEditText.setHint(null);
            } else {
                if (TextUtils.isEmpty(mEditText.getText().toString())) {
                    hideLabel();
                    mEditText.setHint(mHint);
                } else {
                    if (mErrorEnable && !TextUtils.isEmpty(mErrorMessage)) {
                        mLabel.setText(mErrorMessage);
                        mLabel.setTextColor(mErrorTextColor);
                    }
                }
            }
        } else {
            mLabel.setVisibility(GONE);
        }
    }

    void updateCounter(int length) {
        if (mMaxCounter <= 0) {
            mCounterView.setText(String.valueOf(length));
            return;
        }
        boolean wasCounterOverflowed = length >= mMaxCounter;
        if (wasCounterOverflowed) {
            SpannableString spannableString = new SpannableString(length + "/" + mMaxCounter);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(mCounterFlowTextColor);
            spannableString.setSpan(colorSpan, 0, String.valueOf(length).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            mCounterView.setText(spannableString);
        } else {
            mCounterView.setText(length + "/" + mMaxCounter);
        }
    }

    public void setCounterEnabled(boolean enabled) {
        if (mCounterEnabled != enabled) {
            if (enabled) {
                mCounterView = new TextView(getContext());
                mCounterView.setMaxLines(1);
                mCounterView.setSingleLine();
                mCounterView.setTextSize(mCounterTextSize);
                mCounterView.setTextColor(mCounterTextColor);

                addIndicator(mCounterView, -1);
                if (mEditText == null) {
                    updateCounter(0);
                } else {
                    updateCounter(mEditText.getText().length());
                }
            } else {
                removeIndicator(mCounterView);
                mCounterView = null;
            }
            mCounterEnabled = enabled;
        }
    }

    private void addIndicator(TextView indicator, int index) {
        if (mIndicatorArea == null) {
            mIndicatorArea = new LinearLayout(getContext());
            mIndicatorArea.setOrientation(LinearLayout.HORIZONTAL);
            addView(mIndicatorArea, LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);

            // Add a flexible spacer in the middle so that the left/right views stay pinned
            final Space spacer = new Space(getContext());
            final LayoutParams spacerLp = new LayoutParams(0, 0, 1f);
            mIndicatorArea.addView(spacer, spacerLp);

            if (mEditText != null) {
                adjustIndicatorPadding();
            }
        }
        mIndicatorArea.setVisibility(View.VISIBLE);
        mIndicatorArea.addView(indicator, index);
    }


    private void adjustIndicatorPadding() {
        // Add padding to the error and character counter so that they match the EditText
        ViewCompat.setPaddingRelative(mIndicatorArea, ViewCompat.getPaddingStart(mEditText),
            0, ViewCompat.getPaddingEnd(mEditText), mEditText.getPaddingBottom());
    }

    private void removeIndicator(TextView indicator) {
        if (mIndicatorArea != null) {
            mIndicatorArea.removeView(indicator);
            mIndicatorArea.setVisibility(View.GONE);
        }
    }

    /**
     * @return the {@link EditText} text input
     */
    public EditText getEditText() {
        return mEditText;
    }

    /**
     * @return the {@link TextView} label
     */
    public TextView getLabel() {
        return mLabel;
    }

    /**
     * Show the label using an animation
     */
    private void showLabel() {
        if (isShowLabel) {
            return;
        }
        isShowLabel = true;
        mLabel.setVisibility(View.VISIBLE);
        mLabel.setAlpha(0f);
        mLabel.setTranslationY(mLabel.getHeight());
        mLabel.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(ANIMATION_DURATION)
            .setListener(null).start();
    }

    /**
     * Hide the label using an animation
     */
    private void hideLabel() {
        if (!isShowLabel) {
            return;
        }
        isShowLabel = false;
        mEditText.setVisibility(View.INVISIBLE);
        mLabel.setAlpha(1f);
        mLabel.setTranslationY(0f);
        mLabel.animate()
            .alpha(0f)
            .translationY(mLabel.getHeight())
            .setDuration(ANIMATION_DURATION)
            .setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLabel.setVisibility(View.GONE);
                    mEditText.setVisibility(View.VISIBLE);
                }
            }).start();
    }


    public int getLabelTextColor() {
        return mLabelTextColor;
    }

    public void setLabelTextColor(int mLabelTextColor) {
        this.mLabelTextColor = mLabelTextColor;
        mLabel.setTextSize(mLabelTextColor);
        if (mEditText != null) {
            mEditText.setHintTextColor(mLabelTextColor);
        }
    }

    public int getLabelTextSize() {
        return mLabelTextSize;
    }

    public void setLabelTextSize(int mLabelTextSize) {
        this.mLabelTextSize = mLabelTextSize;
        mLabel.setTextSize(mLabelTextSize);
    }


    public CharSequence getHint() {
        return mHint;
    }

    public void setHint(CharSequence mHint) {
        this.mHint = mHint;
        if (mHintEnabled && mEditText != null) {
            mEditText.setHint(mHint);
        }
    }


    public void setMaxCounter(int mMaxCounter) {
        this.mMaxCounter = mMaxCounter;
        updateCounter(mEditText.getText().toString().length());
    }

    public void setHintEnabled(boolean mHintEnabled) {
        this.mHintEnabled = mHintEnabled;
        updateFloatStatus(false);
    }

    public void setErrorEnable(boolean mErrorEnable) {
        this.mErrorEnable = mErrorEnable;
    }

    public void setErrorMessage(String mErrorMessage) {
        this.mErrorMessage = mErrorMessage;
        this.mErrorEnable = true;
    }

    public void setErrorTextColor(int mErrorTextColor) {
        this.mErrorTextColor = mErrorTextColor;
    }

}
