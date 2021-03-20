package com.eazy.uibase.demo.core.style.annotation;

import com.eazy.uibase.demo.core.style.ComponentStyle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Style {
    Class<? extends ComponentStyle> value();
}
