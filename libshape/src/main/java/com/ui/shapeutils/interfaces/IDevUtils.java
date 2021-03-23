package com.ui.shapeutils.interfaces;

import android.view.View;

/**
 * @github https://github.com/LiangLuDev
 */

public interface IDevUtils<T, V extends View> {

    /**
     * 直接设置样式到view
     *
     * @param v 需要设置样式的view
     */
    void into(V v);


    /**
     * 返回Drawable样式
     *
     * @return T
     */
    T build();
}
