package com.eazy.uibase.demo.core;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.TreeMap;

public interface Component {
    @IdRes
    int id();
    @StringRes
    int group();
    @DrawableRes
    int icon();
    @StringRes
    int title();
    @StringRes
    int description();

    Class<? extends ComponentFragment> fragmentClass();

    default ComponentFragment createFragment() throws InstantiationException, IllegalAccessException {
        ComponentFragment fragment = fragmentClass().newInstance();
        fragment.setComponent(this);
        return fragment;
    }
}
