package com.xhb.uibase.demo.core.style;

import com.xhb.uibase.demo.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class IconStyle extends ComponentStyle {

    public IconStyle(Field field) {
        this(field, null, null);
    }

    public IconStyle(Field field, Method getter, Method setter) {
        super(field, getter, setter);
        String[] values = {"0",
                String.valueOf(R.drawable.ic_erase),
                String.valueOf(R.drawable.ic_plus) };
        String[] valueTitles = {"<null>", "ic_erase", "ic_plus"};
        setValues(Arrays.asList(values), Arrays.asList(valueTitles));
    }
}
