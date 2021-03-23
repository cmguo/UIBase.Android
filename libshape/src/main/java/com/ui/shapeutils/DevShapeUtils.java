package com.ui.shapeutils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorRes;

import com.ui.shapeutils.selector.ColorSelector;
import com.ui.shapeutils.selector.DrawableSelector;
import com.ui.shapeutils.shape.DevShape;


/**
 * 代码设置Shape、Selector
 *
 * @github https://github.com/LiangLuDev
 */

public class DevShapeUtils {
    @SuppressLint("StaticFieldLeak")
    private static Application context;

    /**
     * 必须在全局Application先调用，获取context上下文
     *
     * @param app Application
     */
    public static void init(Application app) {
        context = app;
    }


    /**
     * 获取全局上下文
     */
    public static Context getContext() {
        initialize();
        return context;
    }

    /**
     * 检测是否调用初始化方法
     */
    private static void initialize() {
        if (context == null) {
            throw new ExceptionInInitializerError("请先在全局Application中调用 DevShapeUtils.init() 初始化！");
        }
    }


    /**
     * 设置样式（主要是椭圆和矩形）
     *
     * @return OvalShape
     */
    public static DevShape shape(@DevShape.Shape int shapeModel) {
        return DevShape.getInstance(shapeModel);
    }

    /**
     * 背景状态选择器（背景颜色）
     *
     * @param pressedColorResId 触摸颜色 例：R.color.colorPrimary
     * @param normalColorResId  正常颜色 例：R.color.colorPrimary
     * @return DrawableSelector
     */
    public static DrawableSelector selectorBackground(@ColorRes int pressedColorResId, @ColorRes int normalColorResId) {
        return DrawableSelector.getInstance()
                .selectorBackground(new ColorDrawable(DevShapeUtils.getContext().getResources().getColor(pressedColorResId)),
                        new ColorDrawable(DevShapeUtils.getContext().getResources().getColor(normalColorResId)));
    }

    /**
     * 背景状态选择器（背景颜色）
     *
     * @param pressedColorResId  触摸颜色 例：R.color.colorPrimary
     * @param normalColorResId   正常颜色 例：R.color.colorPrimary
     * @param unEnableColorResId 不可用颜色 例：R.color.colorPrimary
     * @return DrawableSelector
     */
    public static DrawableSelector selectorBackground(@ColorRes int pressedColorResId, @ColorRes int normalColorResId, @ColorRes int unEnableColorResId) {
        return DrawableSelector.getInstance()
                .selectorBackground(new ColorDrawable(DevShapeUtils.getContext().getResources().getColor(pressedColorResId)),
                        new ColorDrawable(DevShapeUtils.getContext().getResources().getColor(normalColorResId)),
                        new ColorDrawable(DevShapeUtils.getContext().getResources().getColor(unEnableColorResId)));
    }

    /**
     * .
     * 背景状态选择器（背景颜色）
     *
     * @param pressedColor 触摸颜色 例：#ffffff
     * @param normalColor  正常颜色 例：#ffffff
     * @return DrawableSelector
     */
    public static DrawableSelector selectorBackground(String pressedColor, String normalColor) {
        return DrawableSelector.getInstance()
                .selectorBackground(new ColorDrawable(Color.parseColor(pressedColor)),
                        new ColorDrawable(Color.parseColor(normalColor)));
    }

    /**
     * .
     * 背景状态选择器（背景颜色）
     *
     * @param pressedColor  触摸颜色 例：#ffffff
     * @param normalColor   正常颜色 例：#ffffff
     * @param unEnableColor 不可用颜色 例：#ffffff
     * @return DrawableSelector
     */
    public static DrawableSelector selectorBackground(String pressedColor, String normalColor, String unEnableColor) {
        return DrawableSelector.getInstance()
                .selectorBackground(new ColorDrawable(Color.parseColor(pressedColor)),
                        new ColorDrawable(Color.parseColor(normalColor)),
                        new ColorDrawable(Color.parseColor(unEnableColor)));
    }

    /**
     * .
     * 背景状态选择器（背景Drawable）
     *
     * @param pressedDrawable 触摸颜色 例：Context.getResources.getDrawable(R.drawable/mipmap.xxx)
     * @param normalDrawable  正常颜色 例：Context.getResources.getDrawable(R.drawable/mipmap.xxx)
     * @return DrawableSelector
     */
    public static DrawableSelector selectorBackground(Drawable pressedDrawable, Drawable normalDrawable) {
        return DrawableSelector.getInstance().selectorBackground(pressedDrawable, normalDrawable);
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
    public static DrawableSelector selectorBackground(Drawable pressedDrawable, Drawable normalDrawable, Drawable unEnableDrawable) {
        return DrawableSelector.getInstance().selectorBackground(pressedDrawable, normalDrawable, unEnableDrawable);
    }


    /**
     * 字体颜色颜色器
     *
     * @param pressedColorResId 触摸颜色 例：R.color.colorPrimary
     * @param normalColorResId  正常颜色 例：R.color.colorPrimary
     * @return DrawableSelector
     */
    public static ColorSelector selectorColor(@ColorRes int pressedColorResId, @ColorRes int normalColorResId) {
        return ColorSelector.getInstance()
                .selectorColor(DevShapeUtils.getContext().getResources().getColor(pressedColorResId), DevShapeUtils.getContext().getResources().getColor(normalColorResId));
    }


    /**
     * 字体颜色颜色器
     *
     * @param pressedColor 触摸颜色 例：#ffffff
     * @param normalColor  正常颜色 例：#ffffff
     * @return DrawableSelector
     */
    public static ColorSelector selectorColor(String pressedColor, String normalColor) {
        return ColorSelector.getInstance().selectorColor(Color.parseColor(pressedColor), Color.parseColor(normalColor));
    }

}
