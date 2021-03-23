package com.ui.shapeutils.selector;

import android.content.res.ColorStateList;
import android.widget.TextView;

import com.ui.shapeutils.interfaces.IDevUtils;


/**
 * 字体颜色状态选择器（字体颜色）
 *
 * @github https://github.com/LiangLuDev
 */

public class ColorSelector implements IDevUtils<ColorStateList, TextView> {
    private static ColorSelector mColorSelector;
    //触摸颜色
    private int pressedColor;
    //正常颜色
    private int normalColor;
    //不可用状态下颜色
    private int unEnableColor;


    public static ColorSelector getInstance() {
        mColorSelector = new ColorSelector();
        return mColorSelector;
    }

    /**
     * 背景状态选择器（背景颜色）
     *
     * @param pressedColorResId 触摸颜色 例：R.color.colorPrimary
     * @param normalColorResId  正常颜色 例：R.color.colorPrimary
     * @return DrawableSelector
     */
    public ColorSelector selectorColor(int pressedColorResId, int normalColorResId) {
        this.pressedColor = pressedColorResId;
        this.normalColor = normalColorResId;
        return this;
    }

    /**
     * 背景状态选择器（背景颜色）
     *
     * @param pressedColorResId  触摸颜色 例：R.color.colorPrimary
     * @param normalColorResId   正常颜色 例：R.color.colorPrimary
     * @param unEnableColorResId 不可用状态下颜色 例：R.color.colorPrimary
     * @return DrawableSelector
     */
    public ColorSelector selectorColor(int pressedColorResId, int normalColorResId, int unEnableColorResId) {
        this.pressedColor = pressedColorResId;
        this.normalColor = normalColorResId;
        this.unEnableColor = unEnableColorResId;
        return this;
    }


    @Override
    public void into(TextView textView) {
        textView.setTextColor(createColorSelector());
    }

    @Override
    public ColorStateList build() {
        return createColorSelector();
    }

    /**
     * 创建触摸颜色变化
     *
     * @return ColorStateList
     */
    private ColorStateList createColorSelector() {
        if (unEnableColor == 0) unEnableColor = normalColor;
        int[] colors = new int[]{unEnableColor, pressedColor, normalColor};
        int[][] states = new int[3][];
        states[0] = new int[]{-android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_pressed};
        states[2] = new int[]{};
        return new ColorStateList(states, colors);
    }
}
