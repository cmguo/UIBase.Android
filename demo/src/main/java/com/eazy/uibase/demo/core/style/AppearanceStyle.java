package com.eazy.uibase.demo.core.style;

import com.eazy.uibase.demo.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AppearanceStyle extends ResourceStyle {

    private static final String[] resources = new String[] {
        "style/TipViewToastAppearance",
        "style/TipViewToastAppearance_Opaque",
        "style/TipViewToolTipAppearance",
        "style/TipViewToolTipAppearance_Opaque",
        "style/TipViewToolTipAppearance_Special",
        "style/TipViewToolTipAppearance_Success",
        "style/TipViewToolTipAppearance_Warning",
        "style/TipViewToolTipAppearance_Error",
        "style/TipViewSnackAppearance",
        "style/TipViewSnackAppearance_Info",
        "style/TipViewSnackAppearance_Error",
    };

    public AppearanceStyle(Field field) {
        this(field, null, null);
    }

    public AppearanceStyle(Field field, Method getter, Method setter) {
        super(field, getter, setter, resources);
    }
}
