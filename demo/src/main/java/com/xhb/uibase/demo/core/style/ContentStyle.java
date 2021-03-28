package com.xhb.uibase.demo.core.style;

import com.xhb.uibase.demo.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ContentStyle extends ComponentStyle {

    public static int[] icons = {
        R.drawable.ic_erase,
        R.array.button_icon_text,
        R.style.button_content_style
    };

    public ContentStyle(Field field) {
        this(field, null, null);
    }

    public ContentStyle(Field field, Method getter, Method setter) {
        super(field, getter, setter);
        String[] values = {"0",
            String.valueOf(R.drawable.ic_erase),
            String.valueOf(R.string.app_name),
            String.valueOf(R.array.button_icon_text),
            String.valueOf(R.style.button_content_style)
        };
        String[] valueTitles = {"<null>", "ic_erase", "app_name", "icon_text", "style"};
        setValues(Arrays.asList(values), Arrays.asList(valueTitles));
    }
}
