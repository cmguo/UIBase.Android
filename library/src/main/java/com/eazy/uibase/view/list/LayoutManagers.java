package com.eazy.uibase.view.list;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LayoutManagers {

    @FunctionalInterface
    public interface Builder {
        RecyclerView.LayoutManager build(RecyclerView view);
    }

    public static Builder linear() {
        return (view) -> new LinearLayoutManager(view.getContext());
    }

    public static Builder linear(@RecyclerView.Orientation final int orientation, final boolean reverse) {
        return (view) -> new LinearLayoutManager(view.getContext(), orientation, reverse);
    }

    public static Builder grid(final int spanCount) {
        return (view) -> new GridLayoutManager(view.getContext(), spanCount);
    }

    public static Builder grid(final int spanCount, @RecyclerView.Orientation final int orientation, final boolean reverse) {
        return (view) -> new GridLayoutManager(view.getContext(), spanCount, orientation, reverse);
    }
}
