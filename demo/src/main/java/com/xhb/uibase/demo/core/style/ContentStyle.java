package com.xhb.uibase.demo.core.style;

import com.xhb.uibase.demo.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ContentStyle extends ResourceStyle {

    public static int[] icons = {
        R.drawable.icon_left,
        R.drawable.icon_exit,
        R.array.button_icon_text,
        R.style.button_content_prim_style,
        R.style.button_content_text_style,
    };

    private static final String[] resources = new String[]{
        "drawable/icon_left",
        "drawable/icon_exit",
        "drawable/img_share_weixin",
        "text_confirm:string/confirm",
        "text_cancel:string/cancel",
        "button_array:array/button_icon_text",
        "button_prim:style/button_content_prim_style",
        "button_text:style/button_content_text_style",
        "title_icon:style/title_bar_icon",
        "title_text:style/title_bar_text",
        "title_only:style/title_bar_text_only",
        "layout_text:layout/text_item",
        "xhb_label:layout/xhb_label",
        "layout_tab_bar:layout/xhb_tab_bar",
        "layout_picker:layout/xhb_picker_view",
        "layout_time:layout/xhb_time_picker_view",
    };

    public ContentStyle(Field field) {
        this(field, null, null);
    }

    public ContentStyle(Field field, Method getter, Method setter) {
        super(field, getter, setter, resources);
    }
}
