package com.eazy.uibase.demo.core;

import androidx.databinding.Bindable;

import com.ustc.base.prop.PropertySet;
import com.ustc.base.util.data.Collections;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentStyles {

    private static Map<Class<?>, ComponentStyles> classStyles = new HashMap<>();

    static {
        classStyles.put(ViewStyles.class, new RootComponentStyles());
    }

    public static ComponentStyles get(Class<?> styles) {
        ComponentStyles cs = classStyles.get(styles);
        if (cs != null)
            return cs;
        ComponentStyles csp = get(styles.getSuperclass());
        cs = new ComponentStyles(csp, styles);
        classStyles.put(styles, cs);
        return cs;
    }

    private ComponentStyles parent;
    protected List<ComponentStyle> styles = new ArrayList<>();

    protected ComponentStyles() {}

    private ComponentStyles(ComponentStyles csp, Class<?> styles) {
        parent = csp;
        for (Field f : styles.getDeclaredFields()) {
            int m = f.getModifiers();
            if ((m & Modifier.STATIC) != 0)
                continue;
            if ((m & Modifier.PUBLIC) == 0)
                continue;
            if ((m & Modifier.FINAL) != 0)
                continue;
            if (!f.isAnnotationPresent(Bindable.class))
                continue;
            this.styles.add(new ComponentStyle(f));
        }
    }

    public Collection<ComponentStyle> allStyles() {
        return Collections.join(parent.allStyles(), styles);
    }
}

class RootComponentStyles extends ComponentStyles
{
    public RootComponentStyles() {
        styles = new ArrayList<>();
    }

    @Override
    public Collection<ComponentStyle> allStyles() {
        return styles;
    }
}