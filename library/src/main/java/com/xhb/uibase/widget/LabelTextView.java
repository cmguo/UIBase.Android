package com.xhb.uibase.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.xhb.uibase.R;
import com.xhb.uibase.utils.ScreenUtils;

import cn.xhb.badgeview.BGABadgeView;

public class LabelTextView extends LinearLayout {

    private int rightViewType;
    private static int RIGHT_TEXT_VIEW = 1;
    private static int RIGHT_IMAGE_VIEW = RIGHT_TEXT_VIEW << 1;
    private static int RIGHT_BADGE_VIEW = RIGHT_TEXT_VIEW << 2;
    private static int RIGHT_ARROW_VIEW = RIGHT_TEXT_VIEW << 3;

    private LinearLayout rootView;

    private LinearLayout titleViewContainer;

    /**
     * 最左边的Icon
     */
    private ImageView ivLeftIcon;

    /**
     * 右边的文字
     */
    private TextView tvRightText;

    private String rightText;

    private int rightTextSize;

    private int rightTextColor;

    /**
     * 右边的图片，箭头左侧
     */
    private ImageView ivRightImage;

    /**
     * 右边的小红点
     */
    private BGABadgeView badgeView;

    /**
     * 右边的icon 通常是箭头
     */
    private ImageView ivRightIcon;

    private int internalViewPadding;

    private int paddingVertical;

    private int paddingHorizontal;

    private Drawable leftIconDrawable;

    private Drawable rightImageDrawable;

    private Drawable rightIconDrawable;


    /**
     * 主标题
     */
    private TextView mainTextView;

    private String mainTitle;

    private int mainTitleTextSize;

    private int mainTitleTextColor;

    /**
     * 副标题
     */
    private TextView subTextView;

    private String subtitle;

    private int subtitleTextSize;

    private int subtitleTextColor;


    private int mainAndSubTitlePadding;


    private View bottomLine;
    private boolean bottomLineEnable;
    private int bottomLineHeight;
    private int bottomLineColor;
    private boolean bottomLinePaddingEnable;
    private LayoutParams lineParams;


    public LabelTextView(Context context) {
        this(context, null);
    }

    public LabelTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LabelTextView);
        rightViewType = a.getInt(R.styleable.LabelTextView_rightView, 0);
        mainTitle = a.getString(R.styleable.LabelTextView_mainTitle);
        if (TextUtils.isEmpty(mainTitle)) {
            mainTitle = context.getString(R.string.common_list_item_default_title);
        }
        subtitle = a.getString(R.styleable.LabelTextView_subTitle);
        mainAndSubTitlePadding = a.getDimensionPixelSize(R.styleable.LabelTextView_mainAndSubTitlePadding, ScreenUtils.dp2PxInt(context, 4));
        mainTitleTextSize = a.getDimensionPixelSize(R.styleable.LabelTextView_mainTitleTextSize, 16);
        mainTitleTextColor = a.getColor(R.styleable.LabelTextView_mainTitleTextColor, ResourcesCompat.getColor(getResources(), R.color.bluegrey_800, null));
        subtitleTextSize = a.getDimensionPixelSize(R.styleable.LabelTextView_subtitleTextSize, 12);
        subtitleTextColor = a.getColor(R.styleable.LabelTextView_subtitleTextColor, ResourcesCompat.getColor(getResources(), R.color.bluegrey_800, null));
        rightText = a.getString(R.styleable.LabelTextView_rightTextContent);
        if (TextUtils.isEmpty(rightText)) {
            rightText = context.getString(R.string.common_list_item_default_right_content);
        }
        rightTextSize = a.getDimensionPixelSize(R.styleable.LabelTextView_rightTextViewSize, 16);
        rightTextColor = a.getColor(R.styleable.LabelTextView_rightTextViewColor, ResourcesCompat.getColor(getResources(), R.color.bluegrey_800, null));
        paddingHorizontal = a.getDimensionPixelSize(R.styleable.LabelTextView_paddingHorizontal, ScreenUtils.dp2PxInt(context, 10));
        paddingVertical = a.getDimensionPixelSize(R.styleable.LabelTextView_paddingVertical, ScreenUtils.dp2PxInt(context, 10));
        internalViewPadding = a.getDimensionPixelSize(R.styleable.LabelTextView_internalViewPadding, ScreenUtils.dp2PxInt(context, 10));
        leftIconDrawable = a.getDrawable(R.styleable.LabelTextView_leftIconDrawable);
        rightImageDrawable = a.getDrawable(R.styleable.LabelTextView_rightImageDrawable);
        rightIconDrawable = a.getDrawable(R.styleable.LabelTextView_rightIconDrawable);
        bottomLineEnable = a.getBoolean(R.styleable.LabelTextView_labelBottomLineEnable, false);
        bottomLineColor = a.getColor(R.styleable.LabelTextView_labelBottomLineColor, ResourcesCompat.getColor(getResources(), R.color.bluegrey_800, null));
        bottomLineHeight = a.getDimensionPixelSize(R.styleable.LabelTextView_labelBottomLineHeight, ScreenUtils.dp2PxInt(context, 1));
        bottomLinePaddingEnable = a.getBoolean(R.styleable.LabelTextView_labelBottomLinePaddingEnable, true);
        a.recycle();
        initView();
    }


    /**
     * 初始化各个控件
     */
    public void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.common_list_item_view, this, true);
        rootView = findViewById(R.id.root_view);
        titleViewContainer = findViewById(R.id.ll_text_title_container);
        ivLeftIcon = findViewById(R.id.iv_left_icon);
        mainTextView = findViewById(R.id.tv_main_title);
        subTextView = findViewById(R.id.tv_sub_title);
        tvRightText = findViewById(R.id.tv_right_text);
        ivRightIcon = findViewById(R.id.iv_right_icon);
        badgeView = findViewById(R.id.tv_right_badge);
        ivRightImage = findViewById(R.id.iv_right_image);
        bottomLine = findViewById(R.id.bottom_line);
        badgeView.showCirclePointBadge();
        badgeView.getBadgeViewHelper().setDragable(true);
        badgeView.getBadgeViewHelper().setBadgeBgColorInt(Color.parseColor("#FF0000"));
        rootView.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);

        if ((rightViewType & RIGHT_TEXT_VIEW) > 0) {
            tvRightText.setVisibility(VISIBLE);
        } else {
            tvRightText.setVisibility(GONE);
        }
        if ((rightViewType & RIGHT_IMAGE_VIEW) > 0) {
            ivRightImage.setVisibility(VISIBLE);
        } else {
            ivRightImage.setVisibility(GONE);
        }
        if ((rightViewType & RIGHT_BADGE_VIEW) > 0) {
            badgeView.setVisibility(VISIBLE);
        } else {
            badgeView.setVisibility(GONE);
        }
        if ((rightViewType & RIGHT_ARROW_VIEW) > 0) {
            ivRightIcon.setVisibility(VISIBLE);
        } else {
            ivRightIcon.setVisibility(GONE);
        }
        showLeftIcon(leftIconDrawable != null);
        setMainTitle(mainTitle);
        setMainTitleTextColor(mainTitleTextColor);
        setMainTitleTextSize(mainTitleTextSize);
        setSubtitle(subtitle);
        setSubtitleTextColor(subtitleTextColor);
        setSubtitleTextSize(subtitleTextSize);
        setInternalViewPadding(internalViewPadding);
        setMainAndSubTitlePadding(mainAndSubTitlePadding);
        setRightText(rightText);
        setRightTextColor(rightTextColor);
        setRightTextSize(rightTextSize);
        if (rightImageDrawable != null) {
            ivRightImage.setImageDrawable(rightIconDrawable);
        }
        if (rightIconDrawable != null) {
            ivRightIcon.setImageDrawable(rightIconDrawable);
        }

        lineParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, bottomLineHeight);
        if (bottomLinePaddingEnable) {
            lineParams.leftMargin = paddingHorizontal;
        }
        bottomLine.setLayoutParams(lineParams);
        bottomLine.setBackgroundColor(bottomLineColor);
        setBottomLineEnable(bottomLineEnable);
    }

    public void setInternalViewPadding(int internalViewPadding) {
        this.internalViewPadding = internalViewPadding;
        if (leftIconDrawable != null) {
            titleViewContainer.setPadding(internalViewPadding, 0, 0, 0);
        }
        tvRightText.setPadding(internalViewPadding, 0, 0, 0);
        ivRightIcon.setPadding(internalViewPadding, 0, 0, 0);
        badgeView.setPadding(internalViewPadding, 0, 0, 0);
        ivRightImage.setPadding(internalViewPadding, 0, 0, 0);
    }

    public void setPaddingVertical(int paddingVertical) {
        this.paddingVertical = paddingVertical;
        rootView.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
    }

    public void setPaddingHorizontal(int paddingHorizontal) {
        this.paddingHorizontal = paddingHorizontal;
        rootView.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
    }


    public void setMainAndSubTitlePadding(int mainAndSubTitlePadding) {
        this.mainAndSubTitlePadding = mainAndSubTitlePadding;
        subTextView.setPadding(0, mainAndSubTitlePadding, 0, 0);
    }

    /**
     * 设置左边Icon
     *
     * @param iconRes
     */
    public LabelTextView setLeftIconRes(int iconRes) {
        ivLeftIcon.setImageResource(iconRes);
        return this;
    }

    public LabelTextView setLeftIconDrawable(Drawable leftIconDrawable) {
        this.leftIconDrawable = leftIconDrawable;
        ivLeftIcon.setImageDrawable(leftIconDrawable);
        return this;
    }

    /**
     * 设置左边Icon显示与否
     *
     * @param showLeftIcon
     */
    private void showLeftIcon(boolean showLeftIcon) {
        if (showLeftIcon) {
            ivLeftIcon.setVisibility(VISIBLE);
        } else {
            ivLeftIcon.setVisibility(GONE);
        }
    }

    /**
     * 设置右边Icon 以及Icon的宽高
     */
    public LabelTextView setLeftIconSize(int widthDp, int heightDp) {
        ViewGroup.LayoutParams layoutParams = ivLeftIcon.getLayoutParams();
        layoutParams.width = ScreenUtils.dp2PxInt(getContext(), widthDp);
        layoutParams.height = ScreenUtils.dp2PxInt(getContext(), heightDp);
        ivLeftIcon.setLayoutParams(layoutParams);
        return this;
    }


    /**
     * 设置中间的文字内容
     *
     * @param textContent
     * @return
     */
    public LabelTextView setMainTitle(String textContent) {
        mainTextView.setText(textContent);
        return this;
    }

    /**
     * 设置中间的文字颜色
     *
     * @return
     */
    public LabelTextView setMainTitleTextColorRes(@ColorRes int colorRes) {
        mainTextView.setTextColor(ResourcesCompat.getColor(getResources(), colorRes, null));
        return this;
    }

    public LabelTextView setMainTitleTextColor(@ColorInt int color) {
        mainTextView.setTextColor(color);
        return this;
    }

    /**
     * 设置中间的文字大小
     *
     * @return
     */
    public LabelTextView setMainTitleTextSize(int textSizeSp) {
        mainTextView.setTextSize(textSizeSp);
        return this;
    }

    /**
     * 设置中间的文字内容
     *
     * @param textContent
     * @return
     */
    public LabelTextView setSubtitle(String textContent) {
        if (!TextUtils.isEmpty(textContent)) {
            subTextView.setText(textContent);
            subTextView.setVisibility(VISIBLE);
        } else {
            subTextView.setVisibility(GONE);
        }
        return this;
    }

    /**
     * 设置中间的文字颜色
     *
     * @return
     */
    public LabelTextView setSubtitleTextColorRes(@ColorRes int colorRes) {
        subTextView.setTextColor(ResourcesCompat.getColor(getResources(), colorRes, null));
        return this;
    }

    public LabelTextView setSubtitleTextColor(@ColorInt int color) {
        subTextView.setTextColor(color);
        return this;
    }

    /**
     * 设置中间的文字大小
     *
     * @return
     */
    public LabelTextView setSubtitleTextSize(int textSizeSp) {
        subTextView.setTextSize(textSizeSp);
        return this;
    }

    /**
     * 设置右边文字内容
     *
     * @return
     */
    public LabelTextView setRightText(String rightText) {
        tvRightText.setText(rightText);
        return this;
    }


    /**
     * 设置右边文字颜色
     *
     * @return
     */
    public LabelTextView setRightTextColorRes(@ColorRes int colorRes) {
        tvRightText.setTextColor(getResources().getColor(colorRes));
        return this;
    }

    public LabelTextView setRightTextColor(@ColorInt int color) {
        tvRightText.setTextColor(color);
        return this;
    }

    /**
     * 设置右边文字大小
     *
     * @return
     */
    public LabelTextView setRightTextSize(int textSize) {
        tvRightText.setTextSize(textSize);
        return this;
    }

    /**
     * 设置右箭头的显示与不显示
     *
     * @param showArrow
     */
    public LabelTextView showArrow(boolean showArrow) {
        if (showArrow) {
            ivRightIcon.setVisibility(VISIBLE);
        } else {
            ivRightIcon.setVisibility(GONE);
        }
        return this;
    }

    /**
     * 设置右边icon
     */
    public LabelTextView setRightIconRes(int iconRes) {
        ivRightIcon.setImageResource(iconRes);
        return this;
    }

    public LabelTextView setRightIconDrawable(Drawable rightIconDrawable) {
        this.rightIconDrawable = rightIconDrawable;
        ivRightIcon.setImageDrawable(rightIconDrawable);
        return this;
    }

    /**
     * 获取右边icon
     */
    public ImageView getRightIcon() {

        return ivRightIcon;
    }


    /**
     * 获取右边image
     */
    public ImageView getRightImage() {
        return ivRightImage;
    }

    /**
     * 设置右边icon
     */
    public LabelTextView setRightImageDrawableRes(int iconRes) {
        ivRightImage.setImageResource(iconRes);
        return this;
    }

    public LabelTextView setRightImageDrawable(Drawable rightImageDrawable) {
        this.rightImageDrawable = rightImageDrawable;
        ivRightImage.setImageDrawable(rightImageDrawable);
        return this;
    }

    public int getRightViewType() {
        return rightViewType;
    }

    public ImageView getIvLeftIcon() {
        return ivLeftIcon;
    }

    public TextView getTvRightText() {
        return tvRightText;
    }

    public ImageView getIvRightImage() {
        return ivRightImage;
    }

    public BGABadgeView getBadgeView() {
        return badgeView;
    }

    public ImageView getIvRightIcon() {
        return ivRightIcon;
    }

    public int getInternalViewPadding() {
        return internalViewPadding;
    }

    public int getPaddingVertical() {
        return paddingVertical;
    }

    public int getPaddingHorizontal() {
        return paddingHorizontal;
    }

    public Drawable getLeftIconDrawable() {
        return leftIconDrawable;
    }

    public Drawable getRightImageDrawable() {
        return rightImageDrawable;
    }

    public Drawable getRightIconDrawable() {
        return rightIconDrawable;
    }

    public TextView getMainTextView() {
        return mainTextView;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public int getMainTitleTextSize() {
        return mainTitleTextSize;
    }

    public int getMainTitleTextColor() {
        return mainTitleTextColor;
    }

    public TextView getSubTextView() {
        return subTextView;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getSubtitleTextSize() {
        return subtitleTextSize;
    }

    public int getSubtitleTextColor() {
        return subtitleTextColor;
    }

    public int getMainAndSubTitlePadding() {
        return mainAndSubTitlePadding;
    }

    public LabelTextView setBottomLineEnable(boolean bottomLineEnable) {
        this.bottomLineEnable = bottomLineEnable;
        if (bottomLineEnable) {
            bottomLine.setVisibility(VISIBLE);
        } else {
            bottomLine.setVisibility(GONE);
        }
        return this;
    }

    public LabelTextView setBottomLineHeight(int bottomLineHeight) {
        this.bottomLineHeight = bottomLineHeight;
        lineParams.height = bottomLineHeight;
        return this;
    }

    public LabelTextView setBottomLineColor(@ColorInt int bottomLineColor) {
        this.bottomLineColor = bottomLineColor;
        bottomLine.setBackgroundColor(bottomLineColor);
        return this;
    }

    public LabelTextView setBottomLineColorRes(@ColorRes int bottomLineColor) {
        return setBottomLineColor(ResourcesCompat.getColor(getResources(), bottomLineColor, null));
    }

    public void setBottomLinePaddingEnable(boolean bottomLinePaddingEnable) {
        this.bottomLinePaddingEnable = bottomLinePaddingEnable;
        if (bottomLinePaddingEnable) {
            lineParams.leftMargin = paddingHorizontal;
        }
    }
}
