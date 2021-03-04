package com.eazy.uibase.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eazy.uibase.R;

/**
 * Adding loading ability for Button.
 */
public class ZButtonLoadingView extends FrameLayout {

    private View mLoadingLayout;
    private ProgressBar mLoadingProgressBar;
    private TextView mLoadingText;
    private View mButton;

    public ZButtonLoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        mLoadingLayout = LayoutInflater.from(context).inflate(R.layout.button_loading, null);
        mLoadingProgressBar = mLoadingLayout.findViewById(R.id.loading_progress);
        mLoadingText = mLoadingLayout.findViewById(R.id.loading_text);
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ZButtonLoadingView, 0, 0);
            setAttribute(a);
        }
    }

    public void setStyle(int style) {
        if (style > 0) {
            TypedArray a = getContext().obtainStyledAttributes(style, R.styleable.ZButtonLoadingView);
            setAttribute(a);
        }
    }

    private void setAttribute(TypedArray a) {
        // Set drawable resource
        Drawable drawableRes = a.getDrawable(R.styleable.ZButtonLoadingView_loadingDrawable);
        if (drawableRes != null) {
            mLoadingProgressBar.setIndeterminateDrawable(drawableRes);
        }
        int text = a.getResourceId(R.styleable.ZButtonLoadingView_loadingText, 0);
        if (text > 0) {
            mLoadingText.setText(text);
        } else {
            CharSequence textString = a.getText(R.styleable.ZButtonLoadingView_loadingText);
            mLoadingText.setText(textString);
        }
        if (!TextUtils.isEmpty(mLoadingText.getText())) {
            mLoadingText.setVisibility(VISIBLE);
        } else {
            mLoadingText.setVisibility(GONE);
        }
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 1) {
            throw new InflateException(this.getClass().getSimpleName() +
                    " can only have one text/button child, but current count is " + getChildCount());
        }
        mButton = getChildAt(0);
        FrameLayout.LayoutParams buttonLayoutParams = (LayoutParams) mButton.getLayoutParams();
        buttonLayoutParams.gravity = Gravity.CENTER;
        mButton.setLayoutParams(buttonLayoutParams);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        addView(mLoadingLayout, params);
        mLoadingLayout.setClickable(true);
        setLoading(false);
    }

    public void setLoadingDrawable(int res) {
        mLoadingProgressBar.setIndeterminateDrawable(getResources().getDrawable(res));
    }

    public void setLoadingText(CharSequence value) {
        mLoadingText.setText(value);
    }

    public boolean isLoading() {
        return mLoadingLayout.getVisibility() == VISIBLE;
    }

    public void setLoading(boolean loading) {
        if (this.getBackground() == null) {
            this.setBackground(mButton.getBackground());
            mLoadingLayout.setPadding(mButton.getPaddingLeft(), 0, mButton.getPaddingRight(), 0);
            if (mButton instanceof TextView) {
                final float textSize = ((TextView) mButton).getTextSize();
                mLoadingText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                mLoadingText.setTextColor(((TextView) mButton).getTextColors().getDefaultColor());
                ViewGroup.LayoutParams params = mLoadingProgressBar.getLayoutParams();
                params.width = (int) textSize;
                params.height = (int) textSize;
                mLoadingProgressBar.setLayoutParams(params);
            }
        }
        if (loading) {
            mButton.setVisibility(INVISIBLE);
            mLoadingLayout.setVisibility(VISIBLE);
        } else {
            mButton.setVisibility(VISIBLE);
            mLoadingLayout.setVisibility(GONE);
        }
    }


    @androidx.databinding.BindingAdapter("loading")
    public static <T> void bindLoading(ZButtonLoadingView view, boolean loading) {
        view.setLoading(loading);
    }

    @androidx.databinding.BindingAdapter("style")
    public static <T> void bindStyle(ZButtonLoadingView view, int style) {
        view.setStyle(style);
    }

    public static void setLoading(View buttonView, boolean value) {
        if (buttonView.getParent() instanceof ZButtonLoadingView) {
            bindLoading((ZButtonLoadingView) buttonView.getParent(), value);
        } else {
            throw new RuntimeException("buttonView's parent must be " + ZButtonLoadingView.class.getName() +
                    ", but current is " + buttonView.getParent());
        }
    }


}
