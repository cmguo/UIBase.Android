package com.xhb.uibase.view.list;

import android.view.View;

import androidx.annotation.NonNull;

import java.util.List;

public class PagerAdapter<T> extends androidx.viewpager.widget.PagerAdapter {

    private List<T> pages;

    public PagerAdapter(@NonNull List<T> pages) {
        this.pages = pages;
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }
}
