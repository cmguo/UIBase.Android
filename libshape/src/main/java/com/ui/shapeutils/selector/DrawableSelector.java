package com.ui.shapeutils.selector;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorRes;

import com.ui.shapeutils.DevShapeUtils;
import com.ui.shapeutils.interfaces.IDevUtils;


/**
 * 背景状态选择器（颜色背景、图片背景）
 */

public class DrawableSelector implements IDevUtils<Drawable, View> {
    private static DrawableSelector mDrawableSelector;
    //触摸颜色
    private Drawable mPressedDrawable;
    //正常颜色
    private Drawable mNormalDrawable;
    //不可点击颜色
    private Drawable mUnEnableDrawable = null;
    //是否设置TextView颜色选择器
    private boolean isSelectorColor;
    private ColorStateList mColorStateList;


    public static DrawableSelector getInstance() {
        mDrawableSelector = new DrawableSelector();
        return mDrawableSelector;
    }


    /**
     * .
     * 背景状态选择器（背景Drawable）
     *
     * @param pressedDrawable 触摸颜色 例：Context.getResources.getDrawable(R.drawable/mipmap.xxx)
     * @param normalDrawable  正常颜色 例：Context.getResources.getDrawable(R.drawable/mipmap.xxx)
     * @return DrawableSelector
     */
    public DrawableSelector selectorBackground(Drawable pressedDrawable, Drawable normalDrawable) {
        this.mPressedDrawable = pressedDrawable;
        this.mNormalDrawable = normalDrawable;
        return this;
    }

    /**
     * .
     * 背景状态选择器（背景Drawable）
     *
     * @param pressedDrawable  触摸颜色 例：Context.getResources.getDrawable(R.drawable/mipmap.xxx)
     * @param normalDrawable   正常颜色 例：Context.getResources.getDrawable(R.drawable/mipmap.xxx)
     * @param unEnableDrawable 不可用颜色 例：Context.getResources.getDrawable(R.drawable/mipmap.xxx)
     * @return DrawableSelector
     */
    public DrawableSelector selectorBackground(Drawable pressedDrawable, Drawable normalDrawable, Drawable unEnableDrawable) {
        this.mPressedDrawable = pressedDrawable;
        this.mNormalDrawable = normalDrawable;
        this.mUnEnableDrawable = unEnableDrawable;
        return this;
    }

    /**
     * 设置背景选择器同时设置字体颜色颜色器
     *
     * @param pressedColorResId 触摸颜色 例：R.color.colorPrimary
     * @param normalColorResId  正常颜色 例：R.color.colorPrimary
     * @return DrawableSelector
     */
    public DrawableSelector selectorColor(@ColorRes int pressedColorResId, @ColorRes int normalColorResId) {
        mColorStateList = ColorSelector.getInstance()
                .selectorColor(DevShapeUtils.getContext().getResources().getColor(pressedColorResId), DevShapeUtils.getContext().getResources().getColor(normalColorResId))
                .build();
        this.isSelectorColor = true;
        return this;
    }


    /**
     * 设置背景选择器同时设置字体颜色颜色器
     *
     * @param pressedColor 触摸颜色 例：#ffffff
     * @param normalColor  正常颜色 例：#ffffff
     * @return DrawableSelector
     */
    public DrawableSelector selectorColor(String pressedColor, String normalColor) {
        mColorStateList = ColorSelector.getInstance().selectorColor(Color.parseColor(pressedColor), Color.parseColor(normalColor)).build();
        this.isSelectorColor = true;
        return this;
    }

    /**
     * 设置背景选择器同时设置字体颜色颜色器
     *
     * @param pressedColor  触摸颜色 例：#ffffff
     * @param normalColor   正常颜色 例：#ffffff
     * @param unEnableColor 正常颜色 例：#ffffff
     * @return DrawableSelector
     */
    public DrawableSelector selectorColor(String pressedColor, String normalColor, String unEnableColor) {
        mColorStateList = ColorSelector.getInstance()
                .selectorColor(Color.parseColor(pressedColor), Color.parseColor(normalColor), Color.parseColor(unEnableColor))
                .build();
        this.isSelectorColor = true;
        return this;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void into(View view) {
        view.setBackground(createDrawableSelector());
        if (isSelectorColor) {
            try {
                ((TextView) view).setTextColor(mColorStateList);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ExceptionInInitializerError("设置字体颜色选择器（Selector）请传入TextView（包括Button）！！！");
            }
        }
    }

    /**
     * @return 返回背景Drawable   ps：如果同时设置字体颜色选择器，此处不生效，直接into就行。
     */
    @Override
    public Drawable build() {
        return createDrawableSelector();
    }

    /**
     * 创建触摸颜色变化
     *
     * @return StateListDrawable
     */
    private StateListDrawable createDrawableSelector() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        if (mUnEnableDrawable != null) {
            //必须先设置state_enabled，才会生效
            stateListDrawable.addState(new int[]{-android.R.attr.state_enabled}, mUnEnableDrawable);// 不可用状态的图片
        }
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, mPressedDrawable);// 按下显示的图片
        stateListDrawable.addState(new int[]{}, mNormalDrawable);// 抬起显示的图片
        return stateListDrawable;
    }
}
