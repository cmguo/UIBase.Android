package com.eazy.uibase.view.list;

import androidx.annotation.StyleRes;
import androidx.recyclerview.widget.RecyclerView;

public class ItemDecorations {

    @FunctionalInterface
    public interface Builder {
        RecyclerView.ItemDecoration build(RecyclerView view);
    }

    public static Builder divider(float size) {
        return (view) -> new DividerDecoration(view.getContext(), size);
    }

    public static Builder divider(float size, int color) {
        return (view) -> new DividerDecoration(view.getContext(), size, color);
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