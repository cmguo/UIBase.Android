package com.eazy.uibase.demo.core.style;

import com.eazy.uibase.demo.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class IconStyle extends ResourceStyle {

    public static int[] icons = {
        R.drawable.icon_erase,
        R.drawable.img_share_weixin,
        R.drawable.icon_minus,
        R.drawable.icon_add_book,
        R.drawable.icon_plus,
        R.drawable.icon_more,
        R.drawable.icon_left,
        R.drawable.icon_exit,
        R.drawable.icon_search,
    };

    private static String[] resources = new String[] {
        "drawable/icon_erase", "img_share_weixin",
        "icon_plus", "icon_minus", "icon_add_book", "icon_more", "icon_left", "icon_exit", "icon_search"};

    public IconStyle(Field field) {
        this(field, null, null);
    }

    public IconStyle(Field field, Method getter, Method setter) {
        super(field, getter, setter, resources);
    }
}
