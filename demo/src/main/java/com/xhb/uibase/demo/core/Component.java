package com.xhb.uibase.demo.core;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

import com.xhb.uibase.demo.view.main.ContributionRequestFragment;

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

    default Class<? extends ComponentFragment> fragmentClass() {
        return ContributionRequestFragment.class;
    }

    default ComponentFragment createFragment() throws InstantiationException, IllegalAccessException {
        ComponentFragment fragment = fragmentClass().newInstance();
        fragment.setComponent(this);
        return fragment;
    }
}
