package com.eazy.uibase.demo.core.style;

import android.view.Gravity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class GravityStyle extends ComponentStyle {

    public enum ViewGravity {
        LeftTop(Gravity.START | Gravity.TOP),
        CenterTop(Gravity.CENTER | Gravity.TOP),
        RigthTop(Gravity.END | Gravity.TOP),
        LeftCenter(Gravity.START | Gravity.CENTER),
        Center(Gravity.CENTER),
        RigthCenter(Gravity.END | Gravity.CENTER),
        LeftBottom(Gravity.START | Gravity.BOTTOM),
        CenterBottom(Gravity.CENTER | Gravity.BOTTOM),
        RigthBottom(Gravity.END | Gravity.BOTTOM);

        private int gravity_;

        ViewGravity(int gravity) {
            gravity_ = gravity;
        }
        int gravity() {
            return gravity_;
        }
    }
    
    public GravityStyle(Field field) {
        this(field, null, null);
    }

    public GravityStyle(Field field, Method getter, Method setter) {
        super(field, getter, setter);
        List<String> values = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for (ViewGravity g : ViewGravity.values()) {
            values.add(Integer.toString(g.gravity_));
            titles.add(g.toString());
        }
        setValues(values, titles);
    }

}
