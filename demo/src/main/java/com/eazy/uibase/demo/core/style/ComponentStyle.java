package com.eazy.uibase.demo.core.style;

import android.util.Log;
import android.util.SizeF;

import com.ustc.base.prop.PropValue;
import com.ustc.base.util.reflect.ClassWrapper;
import com.eazy.uibase.demo.BR;
import com.eazy.uibase.demo.core.ViewStyles;
import com.eazy.uibase.demo.core.style.annotation.Description;
import com.eazy.uibase.demo.core.style.annotation.Style;
import com.eazy.uibase.demo.core.style.annotation.Title;
import com.eazy.uibase.demo.core.style.annotation.ValueTitles;
import com.eazy.uibase.demo.core.style.annotation.Values;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class ComponentStyle {

    private static final String TAG = "ComponentStyle";

    private final Field field_;
    private final String name_;
    private final String title_;
    private final String desc_;
    private final Method getter_;
    private final Method setter_;
    private final Class<?> valueType_;
    private List<String> values_;
    private List<String> valueTitles_;

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
        desc_ = name_ + ": " + valueType_.getName() + "\n"
                + (description == null ? "暂无详细信息" : description.value());
        Values values = field.getAnnotation(Values.class);
        if (values != null) {
            values_ = Arrays.asList(values.value());
            ValueTitles valueTitles = field.getAnnotation(ValueTitles.class);
            if (valueTitles != null)
                valueTitles_ = Arrays.asList(valueTitles.value());
        } else if (valueType_.isEnum()) {
            values_ = new ArrayList<>();
            for (Object e : Objects.requireNonNull(valueType_.getEnumConstants())) {
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
        return valueTitles_ == null ? values_ : valueTitles_;
    }

    public void init(ViewStyles styles) {
        if (values_ != null) {
            Object value = getRaw(styles);
            String iv = valueToString(value);
            if (!values_.contains(iv)) {
                values_ = new ArrayList<>(values_);
                values_.add(0, iv);
                if (valueTitles_ != null) {
                    valueTitles_ = new ArrayList<>(valueTitles_);
                    valueTitles_.add(0, "<default>");
                }
            }
        }
    }

    public String get(ViewStyles styles) {
        Object value = getRaw(styles);
        String string = valueToString(value);
        if (valueTitles_ == null) {
            return string;
        } else {
            int i = values_.indexOf(string);
            return i < 0 ? string : valueTitles_.get(i);
        }
    }

    public void set(ViewStyles styles, String string) {
        if (valueTitles_ != null) {
            int i = valueTitles_.indexOf(string);
            if (i >= 0)
                string = values_.get(i);
        }
        Object value = valueFromString(string);
        setRaw(styles, value);
    }

    protected Object getRaw(ViewStyles styles) {
        try {
            return getter_ != null ? getter_.invoke(styles) : field_.get(styles);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Log.w(TAG, "get", e);
            return null;
        }
    }

    protected void setRaw(ViewStyles styles, Object value) {
        try {
            if (setter_ != null)
                setter_.invoke(styles, value);
            else
                field_.set(styles, value);
            styles.notifyPropertyChanged(getFieldBinding(name_));
        } catch (IllegalAccessException | InvocationTargetException e) {
            Log.w(TAG, "set", e);
        }
    }

    protected void setValues(List<String> values, List<String> titles) {
        values_ = values;
        valueTitles_ = titles;
    }

    protected String valueToString(Object value) {
        return PropValue.toString(value);
    }

    protected Object valueFromString(String value) {
        Object value2 = PropValue.fromString(valueType_, value);
        if (value2 == null) {
            if (valueType_ == SizeF.class) {
                value2 = SizeF.parseSizeF(value);
            } else if (valueType_ == Object.class) {
                value2 = value;
            }
        }
        return value2;
    }

    protected String[] styleParams() {
        Style style = field_.getAnnotation(Style.class);
        if (style == null)
            return null;
        return style.params();
    }

    private static final Map<String, Integer> fieldBindings_ = new TreeMap<>();
    private final ClassWrapper<?> classBR = ClassWrapper.wrap(BR.class);

    private int getFieldBinding(String name) {
        Integer b = fieldBindings_.get(name);
        if (b == null) {
            b = classBR.get(name);
            fieldBindings_.put(name, b);
        }
        return b;
    }

    private String upperFirst(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
