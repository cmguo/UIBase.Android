package com.xhb.uibase.widget.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.ColorInt;
import androidx.annotation.IntegerRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.res.ResourcesCompat;

import com.xhb.uibase.R;
import com.xhb.uibase.utils.ScreenUtils;

/**
 * 附带标签的输入框
 */
public class LabelEditText extends RelativeLayout {
    private static final String TAG = "LabelEditText";

    private LinearLayout mContentView;

    private LinearLayout mErrorView;

    private ImageView mLabelImageView;

    private TextView mLabelTextView;

    private ClearEditText mEditText;

    private ToggleButton mPasswordToggleButton;

    private int mLabelPaddingHorizon;

    private int mLabelPaddingVertical;


    /**
     * 是否启用密码显示
     */
    private boolean mPasswordEnable;

    /**
     * 是否展示密码切换按钮
     */
    private boolean mPasswordToggleButtonEnable;


    /**
     * 密码可见图标
     */
    private Drawable mPasswordVisibleDrawable;

    /**
     * 密码不可见图标
     */
    private Drawable mPasswordInvisibleDrawable;

    /**
     * 标签图标
     */
    private Drawable mLabelDrawable;

    /**
     * 标签与图标的间距
     */
    private int mLabelDrawablePadding;

    /**
     * 标签与输入文本的间距
     */
    private int mLabelEditPadding;

    /**
     * passwordToggleButton与输入文本的间距
     */
    private int mEditRightPadding;

    /**
     * LabelText
     */
    private CharSequence mLabelText;

    /**
     * LabelText 字体颜色
     */
    private int mLabelTextColor;

    /**
     * LabelText 字体大小
     */
    private int mLabelTextSize;

    /**
     * LabelText 宽度
     */
    private int mLabelWidth;

    /**
     * 输入内容
     */
    private CharSequence mText;

    /**
     * 输入字体大小
     */
    private int mTextSize;

    /**
     * 输入字体颜色
     */
    private int mTextColor;

    /**
     * 暗提示文本
     */
    private CharSequence mHint;

    /**
     * 暗提示文字颜色
     */
    private int mHintTextColor;

    /**
     * 短信验证码功能
     */
    private boolean mSmsCodeEnable;

    private int mSmsTextEnableColor;

    private int mSmsTextDisableColor;

    private int mSmsTotalTime;

    private String mSmsPreText;

    private String mSmsAfterText;

    private CountDownTextView mSmsTextView;

    /**
     * Picture verification code
     */
    private boolean mImageCodeEnable;

    private Drawable mImageCodeSuccessDrawable;

    private Drawable mImageCodeFailedDrawable;

    private Drawable mImageCodeFailedIcon;

    private Drawable mImageCodeLoadingIcon;

    private ProgressButton mImageCodeView;


    private TextView mErrorTextView;
    private String mErrorMessage;
    private int mErrorTextColor;
    private int mErrorTextSize;

    private View mBottomLine;
    private boolean mBottomLineEnable;
    private int mBottomLineHeight;
    private int mBottomLineColor;

    private int mErrorViewTopMargin;


    private OnSmsCodeClickedListener onSmsCodeClickedListener;

    private OnImageCodeClickedListener onImageCodeClickedListener;


    /**
     * 最多输入限制
     */
    private int mMaxLength;

    private LinearLayout.LayoutParams mLabelParams;
    private LinearLayout.LayoutParams mToggleButtonParams;
    private LinearLayout.LayoutParams mSmsCodeParams;
    private LayoutParams mErrorParams;


    public LabelEditText(Context context) {
        this(context, null);
    }

    public LabelEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initView();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray arrays = getContext().obtainStyledAttributes(attrs, R.styleable.LabelEditText);
        setPasswordEnable(arrays.getBoolean(R.styleable.LabelEditText_passwordEnable, false));
        mPasswordToggleButtonEnable = arrays.getBoolean(R.styleable.LabelEditText_passwordToggleButtonEnable, false);
        mPasswordVisibleDrawable = arrays.getDrawable(R.styleable.LabelEditText_passwordVisibleDrawable);
        mPasswordInvisibleDrawable = arrays.getDrawable(R.styleable.LabelEditText_passwordInvisibleDrawable);
        if (mPasswordVisibleDrawable == null) {
            mPasswordVisibleDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.common_edittext_password_visible_icon, null);
        }
        if (mPasswordInvisibleDrawable == null) {
            mPasswordInvisibleDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.common_edittext_password_invisible_icon, null);
        }
        mLabelPaddingHorizon = arrays.getDimensionPixelSize(R.styleable.LabelEditText_labelPaddingHorizon, 0);
        mLabelPaddingVertical = arrays.getDimensionPixelSize(R.styleable.LabelEditText_labelPaddingVertical, 0);
        mLabelDrawable = arrays.getDrawable(R.styleable.LabelEditText_labelDrawable);
        mLabelDrawablePadding = arrays.getDimensionPixelSize(R.styleable.LabelEditText_labelDrawablePadding, ScreenUtils.dp2PxInt(getContext(), 10));
        mLabelEditPadding = arrays.getDimensionPixelSize(R.styleable.LabelEditText_labelEditPadding, ScreenUtils.dp2PxInt(getContext(), 30));
        mEditRightPadding = arrays.getDimensionPixelSize(R.styleable.LabelEditText_editRightPadding, ScreenUtils.dp2PxInt(getContext(), 10));
        mLabelText = arrays.getString(R.styleable.LabelEditText_labelText);
        mLabelTextColor = arrays.getColor(R.styleable.LabelEditText_labelTextColor, ResourcesCompat.getColor(getResources(), R.color.static_bluegrey_800, null));
        mLabelTextSize = arrays.getDimensionPixelSize(R.styleable.LabelEditText_labelTextSize, 16);
        mLabelWidth = arrays.getDimensionPixelSize(R.styleable.LabelEditText_labelWidth, LayoutParams.WRAP_CONTENT);
        mText = arrays.getString(R.styleable.LabelEditText_text);
        mTextSize = arrays.getDimensionPixelSize(R.styleable.LabelEditText_textSize, 16);
        mTextColor = arrays.getColor(R.styleable.LabelEditText_textColor, ResourcesCompat.getColor(getResources(), R.color.static_bluegrey_800, null));
        mHint = arrays.getString(R.styleable.LabelEditText_hint);
        mHintTextColor = arrays.getColor(R.styleable.LabelEditText_hintTextColor, ResourcesCompat.getColor(getResources(), R.color.static_bluegrey_800, null));
        mMaxLength = arrays.getInt(R.styleable.LabelEditText_maxLength, -1);
        mSmsTextEnableColor = arrays.getColor(R.styleable.LabelEditText_smsTextEnableColor, -1);
        mSmsTextDisableColor = arrays.getColor(R.styleable.LabelEditText_smsTextDisableColor, -1);
        mSmsTotalTime = arrays.getInteger(R.styleable.LabelEditText_smsTotalTime, 60);
        mSmsPreText = arrays.getString(R.styleable.LabelEditText_smsPreText);
        mSmsAfterText = arrays.getString(R.styleable.LabelEditText_smsAfterText);
        mSmsCodeEnable = arrays.getBoolean(R.styleable.LabelEditText_smsCodeEnable, false);
        mImageCodeEnable = arrays.getBoolean(R.styleable.LabelEditText_imageCodeEnable, false);
        mErrorMessage = arrays.getString(R.styleable.LabelEditText_errorMessage);
        mErrorTextColor = arrays.getColor(R.styleable.LabelEditText_hintTextColor, ResourcesCompat.getColor(getResources(), R.color.red_600, null));
        mErrorTextSize = arrays.getDimensionPixelSize(R.styleable.LabelEditText_errorTextSize, 12);
        mBottomLineEnable = arrays.getBoolean(R.styleable.LabelEditText_bottomLineEnable, false);
        mBottomLineHeight = arrays.getDimensionPixelSize(R.styleable.LabelEditText_bottomLineHeight, ScreenUtils.dp2PxInt(getContext(), 1));
        mBottomLineColor = arrays.getColor(R.styleable.LabelEditText_bottomLineBgColor, ResourcesCompat.getColor(getResources(), R.color.static_bluegrey_800, null));
        mErrorViewTopMargin = arrays.getDimensionPixelSize(R.styleable.LabelEditText_errorViewTopMargin, ScreenUtils.dp2PxInt(getContext(), 5));
        arrays.recycle();
    }


    private void initView() {
//        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);
        mContentView = new LinearLayout(getContext());
        mContentView.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mContentView.setOrientation(LinearLayout.HORIZONTAL);
        mContentView.setGravity(Gravity.CENTER_VERTICAL);
        mContentView.setPadding(mLabelPaddingHorizon, mLabelPaddingVertical, mLabelPaddingHorizon, mLabelPaddingVertical);
        addView(mContentView);
        mContentView.setId(R.id.common_label_text_content_view);
        showLabelDrawable();
        showLabel();
        showEditText();
        showPasswordToggleButton();
        showSmsCode();
        showImageCode();

        showErrorStyle();
        showBottomLine();
    }

    private void showLabelDrawable() {
        if (mLabelImageView == null) {
            mLabelImageView = new ImageView(getContext());
            mContentView.addView(mLabelImageView);
        }
        if (mLabelDrawable != null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.sp2px(getContext(), mLabelTextSize), ScreenUtils.sp2px(getContext(), mLabelTextSize));
            params.rightMargin = mLabelDrawablePadding;
            mLabelImageView.setLayoutParams(params);
            mLabelImageView.setImageDrawable(mLabelDrawable);
            mLabelImageView.setVisibility(VISIBLE);
        } else {
            mLabelImageView.setVisibility(GONE);
        }
    }

    private void showLabel() {
        if (mLabelTextView == null) {
            mLabelTextView = new TextView(getContext());
            mContentView.addView(mLabelTextView);
        }
        if (!TextUtils.isEmpty(mLabelText)) {
            mLabelTextView.setText(mLabelText);
            mLabelTextView.setMaxEms(5);
            mLabelTextView.setSingleLine(true);
            mLabelParams = new LinearLayout.LayoutParams(mLabelWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            mLabelParams.rightMargin = mLabelEditPadding;
            mLabelTextView.setLayoutParams(mLabelParams);
            mLabelTextView.setTextSize(mLabelTextSize);
            mLabelTextView.setTextColor(mLabelTextColor);
            mLabelTextView.setVisibility(VISIBLE);
        } else {
            mLabelTextView.setVisibility(GONE);
        }
    }

    private void showEditText() {
        if (mEditText == null) {
            mEditText = new ClearEditText(getContext());
            mContentView.addView(mEditText);
        }
        mEditText.setMaxLines(1);
        mEditText.setSingleLine(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        mEditText.setLayoutParams(params);
        if (!TextUtils.isEmpty(mHint)) {
            mEditText.setHint(mHint);
            mEditText.setHintTextColor(mHintTextColor);
        }
        if (mMaxLength != -1) {
            setMaxLength(mMaxLength);
        }
        mEditText.setEllipsize(TextUtils.TruncateAt.END);
        mEditText.setTextSize(mTextSize);
        mEditText.setTextColor(mTextColor);
        mEditText.setText(mText);
        mEditText.setBackgroundColor(Color.TRANSPARENT);
    }


    private void showPasswordToggleButton() {
        if (mPasswordToggleButton == null) {
            mPasswordToggleButton = new ToggleButton(getContext());
            mContentView.addView(mPasswordToggleButton);
        }
        if (mPasswordToggleButtonEnable) {
            mPasswordToggleButton.setVisibility(VISIBLE);
            mPasswordToggleButton.setTextOff("");
            mPasswordToggleButton.setTextOn("");
            mToggleButtonParams = new LinearLayout.LayoutParams(ScreenUtils.dp2PxInt(getContext(), 24), ScreenUtils.dp2PxInt(getContext(), 20));
            mToggleButtonParams.leftMargin = mEditRightPadding;
            mPasswordToggleButton.setLayoutParams(mToggleButtonParams);
            // 初始密码不可见
            mPasswordToggleButton.setBackgroundDrawable(mPasswordInvisibleDrawable);
            mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mPasswordToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        //如果选中，显示密码
                        mPasswordToggleButton.setBackgroundDrawable(mPasswordVisibleDrawable);
                        mEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    } else {
                        //否则隐藏密码
                        mPasswordToggleButton.setBackgroundDrawable(mPasswordInvisibleDrawable);
                        mEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                    //把光标设置到当前文本末尾
                    mEditText.setSelection(mEditText.getText().length());
                }
            });
        } else {
            mPasswordToggleButton.setVisibility(GONE);
        }
    }

    private void showSmsCode() {
        if (mSmsTextView == null) {
            mSmsTextView = new CountDownTextView(getContext());
            mContentView.addView(mSmsTextView);
        }
        if (mSmsCodeEnable) {
            mSmsTextView.setVisibility(VISIBLE);
            if (mSmsTextDisableColor != -1) {
                mSmsTextView.setDisableColor(mSmsTextDisableColor);
            }
            if (mSmsTextEnableColor != -1) {
                mSmsTextView.setEnableColor(mSmsTextEnableColor);
            }

            if (!TextUtils.isEmpty(mSmsPreText)) {
                mSmsTextView.setPreText(mSmsPreText);
            }

            if (!TextUtils.isEmpty(mSmsAfterText)) {
                mSmsTextView.setAfterText(mSmsAfterText);
            }
            mSmsTextView.setTime(mSmsTotalTime);
            mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            mSmsCodeParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mSmsCodeParams.leftMargin = mEditRightPadding;
            mSmsTextView.setLayoutParams(mSmsCodeParams);
            mSmsTextView.setOnCountDownClickedListener(new CountDownTextView.OnCountDownClickedListener() {
                @Override
                public void clicked() {
                    if (onSmsCodeClickedListener != null) {
                        onSmsCodeClickedListener.click();
                    }
                }
            });
        } else {
            mSmsTextView.setVisibility(GONE);
        }
    }

    private void showImageCode() {
        if (mImageCodeView == null) {
            mImageCodeView = new ProgressButton(getContext());
            mContentView.addView(mImageCodeView);
        }
        if (mImageCodeEnable) {
            mImageCodeView.setVisibility(VISIBLE);
            mImageCodeView.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.dp2PxInt(getContext(), 80), ScreenUtils.dp2PxInt(getContext(), 34)));
            mImageCodeView.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.common_image_code_success, null));
            mImageCodeView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mImageCodeView.startRotate();
                    if (onImageCodeClickedListener != null) {
                        onImageCodeClickedListener.click();
                    }
                }
            });
        } else {
            mImageCodeView.setVisibility(GONE);
        }
    }

    private void showErrorStyle() {
        if (mErrorView == null) {
            mErrorView = new LinearLayout(getContext());
            mErrorParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mErrorParams.addRule(RelativeLayout.ALIGN_BOTTOM, mContentView.getId());
            mErrorParams.topMargin = mErrorViewTopMargin;
            mErrorView.setLayoutParams(mErrorParams);
            mErrorView.setGravity(Gravity.CENTER_VERTICAL);
            mErrorView.setOrientation(LinearLayout.VERTICAL);
            addView(mErrorView);
            mErrorTextView = new TextView(getContext());
            mErrorTextView.setPadding(mLabelPaddingHorizon, 0, 0, 0);
            mErrorTextView.setSingleLine();
            mErrorView.addView(mErrorTextView);
        }
        if (!TextUtils.isEmpty(mErrorMessage)) {
            mErrorTextView.setTextColor(mErrorTextColor);
            mErrorTextView.setTextSize(mErrorTextSize);
            mErrorTextView.setText(mErrorMessage);
            mErrorTextView.setVisibility(VISIBLE);
            showBottomLine(true);
        } else {
            mErrorTextView.setVisibility(GONE);
            showBottomLine(false);
        }
    }

    private void showBottomLine() {
        showBottomLine(false);
    }

    private void showBottomLine(boolean isShowBottomLine) {
        if (mBottomLineEnable || isShowBottomLine) {
            if (mBottomLine == null) {
                mBottomLine = new View(getContext());
                mBottomLine.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mBottomLineHeight));
                mErrorView.addView(mBottomLine);
                mBottomLine.setBackgroundColor(mErrorTextColor);
            }
            if (!TextUtils.isEmpty(mErrorMessage)) {
                mBottomLine.setBackgroundColor(mErrorTextColor);
            } else {
                mBottomLine.setBackgroundColor(mBottomLineColor);
            }
        }
    }


    public TextView getLabelTextView() {
        return mLabelTextView;
    }


    public EditText getEditText() {
        return mEditText;
    }

    public boolean isPasswordEnable() {
        return mPasswordEnable;
    }

    public void setPasswordEnable(boolean mPasswordEnable) {
        this.mPasswordEnable = mPasswordEnable;
        if (mPasswordEnable) {
            mEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    public boolean isPasswordToggleButtonEnable() {
        return mPasswordToggleButtonEnable;
    }

    public void setPasswordToggleButtonEnable(boolean mPasswordToggleButtonEnable) {
        this.mPasswordToggleButtonEnable = mPasswordToggleButtonEnable;
        showPasswordToggleButton();
    }

    public Drawable getPasswordVisibleDrawable() {
        return mPasswordVisibleDrawable;
    }

    public void setPasswordVisibleDrawable(Drawable mPasswordVisibleDrawable) {
        this.mPasswordVisibleDrawable = mPasswordVisibleDrawable;

    }

    public Drawable getPasswordInvisibleDrawable() {
        return mPasswordInvisibleDrawable;
    }

    public void setPasswordInvisibleDrawable(Drawable mPasswordInvisibleDrawable) {
        this.mPasswordInvisibleDrawable = mPasswordInvisibleDrawable;
    }

    public Drawable getLabelDrawable() {
        return mLabelDrawable;
    }

    public void setLabelDrawable(Drawable mLabelDrawable) {
        this.mLabelDrawable = mLabelDrawable;
        if (mLabelImageView != null) {
            mLabelImageView.setImageDrawable(mLabelDrawable);
        }
    }

    public int getLabelDrawablePadding() {
        return mLabelDrawablePadding;
    }

    public void setLabelDrawablePadding(int mLabelDrawablePadding) {
        this.mLabelDrawablePadding = mLabelDrawablePadding;
        if (mPasswordToggleButtonEnable) {
            mToggleButtonParams.rightMargin = mLabelDrawablePadding;
        }
    }

    public int getLabelEditPadding() {
        return mLabelEditPadding;
    }

    public void setLabelEditPadding(int mLabelEditPadding) {
        this.mLabelEditPadding = mLabelEditPadding;
    }

    public void setErrorMessage(String mErrorMessage) {
        this.mErrorMessage = mErrorMessage;
        showErrorStyle();
    }

    public int getEditRightPadding() {
        return mEditRightPadding;
    }

    public void setEditRightPadding(int mEditRightPadding) {
        this.mEditRightPadding = mEditRightPadding;
        if (mPasswordToggleButtonEnable) {
            mToggleButtonParams.leftMargin = mEditRightPadding;
        }
        if (mSmsCodeEnable) {
            mSmsCodeParams.leftMargin = mEditRightPadding;
        }

    }

    public CharSequence getLabelText() {
        return mLabelText;
    }

    public void setLabelText(CharSequence mLabelText) {
        this.mLabelText = mLabelText;
        mLabelTextView.setText(mLabelText);
    }

    public int getLabelTextColor() {
        return mLabelTextColor;
    }

    public void setLabelTextColor(int mLabelTextColor) {
        this.mLabelTextColor = mLabelTextColor;
        mLabelTextView.setTextColor(mLabelTextColor);
    }

    public int getLabelTextSize() {
        return mLabelTextSize;
    }

    public void setLabelTextSize(int mLabelTextSize) {
        this.mLabelTextSize = mLabelTextSize;
        if (mLabelTextView != null) {
            mLabelTextView.setTextSize(mLabelTextSize);
        }
    }

    public int getLabelWidth() {
        return mLabelWidth;
    }

    public void setLabelWidth(int mLabelWidth) {
        this.mLabelWidth = mLabelWidth;
        if (mLabelTextView != null) {
            mLabelParams.width = mLabelWidth;
        }
    }


    public CharSequence getText() {
        return mText;
    }

    public void setText(CharSequence mText) {
        this.mText = mText;
        mEditText.setText(mText);
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(@IntegerRes int mTextSize) {
        this.mTextSize = mTextSize;
        mEditText.setTextSize(mTextSize);
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(@ColorInt int mTextColor) {
        this.mTextColor = mTextColor;
        mEditText.setTextColor(mTextColor);
    }

    public CharSequence getHint() {
        return mHint;
    }

    public void setHint(CharSequence mHint) {
        this.mHint = mHint;
        mEditText.setHint(mHint);
    }

    public int getHintTextColor() {
        return mHintTextColor;
    }

    public void setHintTextColor(int mHintTextColor) {
        this.mHintTextColor = mHintTextColor;
        mEditText.setHintTextColor(mHintTextColor);
    }

    public void setErrorViewTopMargin(int mErrorViewTopMargin) {
        this.mErrorViewTopMargin = mErrorViewTopMargin;
        mErrorParams.topMargin = mErrorViewTopMargin;
    }

    public int getMaxLength() {
        return mMaxLength;
    }

    public void setMaxLength(int mMaxLength) {
        this.mMaxLength = mMaxLength;
        mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mMaxLength)});
    }

    public CountDownTextView getSmsTextView() {
        return mSmsTextView;
    }

    public interface OnSmsCodeClickedListener {
        void click();
    }

    public interface OnImageCodeClickedListener {
        void click();
    }

    public void setOnSmsCodeClickedListener(OnSmsCodeClickedListener onSmsCodeClickedListener) {
        this.onSmsCodeClickedListener = onSmsCodeClickedListener;
    }

    public void setOnImageCodeClickedListener(OnImageCodeClickedListener onImageCodeClickedListener) {
        this.onImageCodeClickedListener = onImageCodeClickedListener;
    }
}
