package com.eazy.uibase.view.list;


import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.StyleRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ItemDecorations {

    @FunctionalInterface
    public interface Builder {
        RecyclerView.ItemDecoration build(RecyclerView view);
    }

    public static Builder divider(int orientation, float size) {
        return (view) -> new DividerDecoration(view.getContext(), orientation, size);
    }

    public static Builder divider(int orientation, float size, float endianSize) {
        return (view) -> new DividerDecoration(view.getContext(), orientation, size, endianSize);
    }

    public static Builder divider(int orientation, float size, int color) {
        return (view) -> new DividerDecoration(view.getContext(), orientation, size, color);
    }

    public static Builder divider(int orientation, float size, int color, float endianSize) {
        return (view) -> new DividerDecoration(view.getContext(), orientation, size, color, endianSize);
    }

    public static Builder background(float radius, int color) {
        return (view) -> new BackgroundDecoration(view.getContext(), radius, color);
    }

    public static Builder background(float radius, int color, boolean outer) {
        return (view) -> new BackgroundDecoration(view.getContext(), radius, color, outer);
    }

    public static Builder background(@StyleRes int roundStyle) {
        return (view) -> new BackgroundDecoration(view.getContext(), roundStyle);
    }

    public static Builder background(@StyleRes int roundStyle, boolean outer) {
        return (view) -> new BackgroundDecoration(view.getContext(), roundStyle, outer);
    }

}