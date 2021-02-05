package com.eazy.uibase.demo.core;

import android.util.Log;

import androidx.databinding.DataBindingUtil;

import com.ustc.base.prop.PropValue;
import com.ustc.base.util.reflect.ClassWrapper;
import com.eazy.uibase.demo.BR;
import com.eazy.uibase.demo.core.annotation.Values;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ComponentStyle {

    private static final String TAG = "ComponentStyle";

    private Field field_;
    private String title_;
    private String name_;
    private Class<?> valueType_;
    private List<String> values_;

    public ComponentStyle(Field field) {
        field_ = field;
        title_ = field.getName();
        name_ = field.getName();
        valueType_ = field.getType();
        Values values = field.getAnnotation(Values.class);
        if (values != null) {
            values_ = Arrays.asList(values.value());
        } else if (valueType_.isEnum()) {
            values_ = new ArrayList<>();
            for (Object e : valueType_.getEnumConstants()) {
                values_.add(e.toString());
            }
        }
    }

    public String getTitle() {
        return title_;
    }

    public String getName() {
        return name_;
    }

    public Class<?> getValueType() {
        return valueType_;
    }

    public List<String> getValues() {
        return values_;
    }

    public String get(ViewStyles styles) {
        try {
            return PropValue.toString(field_.get(styles));
        } catch (IllegalAccessException e) {
            Log.w(TAG, "get", e);
            return "";
        }
    }

    public void set(ViewStyles styles, String value) {
        try {
            field_.set(styles, PropValue.fromString(valueType_, value));
            styles.notifyPropertyChanged(getFieldBinding(name_));
        } catch (IllegalAccessException e) {
            Log.w(TAG, "set", e);
        }
    }

    private static Map<String, Integer> fieldBindings_ = new TreeMap<>();
    private ClassWrapper<?> classBR = ClassWrapper.wrap(BR.class);

    private int getFieldBinding(String name) {
        Integer b = fieldBindings_.get(name);
        if (b == null) {
            b = (Integer) classBR.get(name);
            fieldBindings_.put(name, b);
        }
        return b;
    }
}
