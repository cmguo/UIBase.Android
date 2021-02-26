package com.xhb.uibase.demo.core;

import android.util.Log;

import androidx.databinding.DataBindingUtil;

import com.ustc.base.prop.PropValue;
import com.ustc.base.util.reflect.ClassWrapper;
import com.xhb.uibase.demo.BR;
import com.xhb.uibase.demo.core.annotation.Description;
import com.xhb.uibase.demo.core.annotation.Title;
import com.xhb.uibase.demo.core.annotation.Values;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ComponentStyle {

    private static final String TAG = "ComponentStyle";

    private Field field_;
    private String name_;
    private String title_;
    private String desc_;
    private Method getter_;
    private Method setter_;
    private Class<?> valueType_;
    private List<String> values_;

    public ComponentStyle(Field field) {
        this(field, null, null);
    }

    public ComponentStyle(Field field, Method getter, Method setter) {
        field_ = field;
        name_ = field.getName();
        getter_ = getter;
        setter_ = setter;
        valueType_ = field.getType();
        Title title = field.getAnnotation(Title.class);
        title_ = title == null ? upperFirst(name_) : title.value();
        Description description = field.getAnnotation(Description.class);
        desc_ = description == null ? name_ : description.value();
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

    public String getName() {
        return name_;
    }

    public String getTitle() {
        return title_;
    }

    public String getDesc() {
        return desc_;
    }

    public Class<?> getValueType() {
        return valueType_;
    }

    public List<String> getValues() {
        return values_;
    }

    public String get(ViewStyles styles) {
        try {
            Object value = getter_ != null ? getter_.invoke(styles) : field_.get(styles);
            return PropValue.toString(value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Log.w(TAG, "get", e);
            return "";
        }
    }

    public void set(ViewStyles styles, String value) {
        try {
            Object value2 = PropValue.fromString(valueType_, value);
            if (setter_ != null)
                setter_.invoke(styles, value2);
            else
                field_.set(styles, value2);
            styles.notifyPropertyChanged(getFieldBinding(name_));
        } catch (IllegalAccessException | InvocationTargetException e) {
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

    private String upperFirst(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
