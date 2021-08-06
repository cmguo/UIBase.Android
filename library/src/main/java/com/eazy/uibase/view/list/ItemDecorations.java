package com.eazy.uibase.view.list;

import androidx.annotation.StyleRes;
import androidx.recyclerview.widget.RecyclerView;

public class ItemDecorations {

    @FunctionalInterface
    public interface Builder {
        default RecyclerView.ItemDecoration build(RecyclerView view) {
            if (density == 0f) {
                density = view.getContext().getResources().getDisplayMetrics().density;
            }
            return build2(view);
        }
        RecyclerView.ItemDecoration build2(RecyclerView view);
    }

    public static Builder divider(float size) {
        return (view) -> new DividerDecoration(px(size));
    }

    public static Builder divider(float size, float endianSize) {
        return (view) -> new DividerDecoration(px(size), px(endianSize));
    }

    public static Builder divider(float size, int color) {
        return (view) -> new DividerDecoration(px(size), color);
    }

    public static Builder divider(float size, int color, float endianSize) {
        return (view) -> new DividerDecoration(px(size), color, px(endianSize));
    }

    public static Builder dividerPx(float size) {
        return (view) -> new DividerDecoration(size);
    }

    public static Builder dividerPx(float size, float endianSize) {
        return (view) -> new DividerDecoration(size, endianSize);
    }

    public static Builder dividerPx(float size, int color) {
        return (view) -> new DividerDecoration(size, color);
    }

    public static Builder dividerPx(float size, int color, float endianSize) {
        return (view) -> new DividerDecoration(size, color, endianSize);
    }

    public static Builder background(float radius, int color) {
        return (view) -> new BackgroundDecoration(px(radius), color);
    }

    public static Builder background(float radius, int color, boolean outer) {
        return (view) -> new BackgroundDecoration(px(radius), color, outer);
    }

    public static Builder backgroundPx(float radius, int color) {
        return (view) -> new BackgroundDecoration(radius, color);
    }

    public static Builder backgroundPx(float radius, int color, boolean outer) {
        return (view) -> new BackgroundDecoration(radius, color, outer);
    }

    public static Builder background(@StyleRes int roundStyle) {
        return (view) -> new BackgroundDecoration(view.getContext(), roundStyle);
    }

    public static Builder background(@StyleRes int roundStyle, boolean outer) {
        return (view) -> new BackgroundDecoration(view.getContext(), roundStyle, outer);
    }

    private static float density = 0f;

    private static float px(float dp) {
        return dp * density;
    }

}