package com.eazy.uibase.utils;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Generic {

    public static <T> Class<T> getParamType(Class<?> clazz, Class<?> base, int index) {
        Type superclass = clazz.getGenericSuperclass();
        while (superclass != null) {
            if (superclass instanceof ParameterizedType) {
                if (((ParameterizedType) superclass).getRawType() == base)
                    break;
                superclass = ((Class) ((ParameterizedType) superclass).getRawType()).getGenericSuperclass();
            } else {
                superclass = ((Class) superclass).getGenericSuperclass();
            }
        }
        if (superclass == null) {
            throw new RuntimeException("Not found base!");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        Type type = parameterized.getActualTypeArguments()[index];
        if (type instanceof ParameterizedType)
            return (Class<T>) ((ParameterizedType) type).getRawType();
        else
            return (Class<T>) type;

    }

}
