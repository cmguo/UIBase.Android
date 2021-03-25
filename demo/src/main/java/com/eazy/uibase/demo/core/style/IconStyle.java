package com.eazy.uibase.demo.core.style;

import com.eazy.uibase.demo.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class IconStyle extends ComponentStyle {

    public static int[] icons = {
        R.drawable.ic_erase,
        R.drawable.icon_minus,
        R.drawable.icon_plus,
        R.drawable.icon_more
    };

    public IconStyle(Field field) {
        this(field, null, null);
    }

    public IconStyle(Field field, Method getter, Method setter) {
        super(field, getter, setter);
        String[] values = {"0",
            String.valueOf(R.drawable.ic_erase),
            String.valueOf(R.drawable.icon_plus),
            String.valueOf(R.drawable.icon_minus) };
        String[] valueTitles = {"<null>", "ic_erase", "icon_plus", "icon_minus"};
        setValues(Arrays.asList(values), Arrays.asList(valueTitles));
    }
}
