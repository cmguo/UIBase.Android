package com.eazy.uibase.demo.core.style.annotation;

import android.graphics.Color;

import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.style.ComponentStyle;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class TextAppearanceStyle extends ComponentStyle {

    public TextAppearanceStyle(Field field) {
        this(field, null, null);
    }

    public TextAppearanceStyle(Field field, Method getter, Method setter) {
        super(field, getter, setter);
        String[] values = {"0",
            String.valueOf(R.style.TextAppearance_MaterialComponents_Headline1),
            String.valueOf(R.style.TextAppearance_MaterialComponents_Headline2),
            String.valueOf(R.style.TextAppearance_MaterialComponents_Headline3),
            String.valueOf(R.style.TextAppearance_MaterialComponents_Headline4),
            String.valueOf(R.style.TextAppearance_MaterialComponents_Headline5),
            String.valueOf(R.style.TextAppearance_MaterialComponents_Headline6),
            String.valueOf(R.style.TextAppearance_Z_Head2_Demo)
        };
        String[] valueTitles = {"<null>", "head1", "head2", "head3", "head4", "head5", "head6", "demo" };
        setValues(Arrays.asList(values), Arrays.asList(valueTitles));
    }
}
