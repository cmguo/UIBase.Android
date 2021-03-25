package com.eazy.uibase.view.list;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LayoutManagers {

    public static LayoutManagerFactory linear() {
        return new LayoutManagerFactory() {
            @Override
            public RecyclerView.LayoutManager create(RecyclerView view) {
                return new LinearLayoutManager(view.getContext());
            }
        };
    }

    public static LayoutManagerFactory linear(@RecyclerView.Orientation final int orientation, final boolean reverse) {
        return new LayoutManagerFactory() {
            @Override
            public RecyclerView.LayoutManager create(RecyclerView view) {
                return new LinearLayoutManager(view.getContext(), orientation, reverse);
            }
        };
    }

    public static LayoutManagerFactory grid(final int spanCount) {
        return new LayoutManagerFactory() {
            @Override
            public RecyclerView.LayoutManager create(RecyclerView view) {
                return new GridLayoutManager(view.getContext(), spanCount);
            }
        };
    }

    public static LayoutManagerFactory grid(final int spanCount, @RecyclerView.Orientation final int orientation, final boolean reverse) {
        return new LayoutManagerFactory() {
            @Override
            public RecyclerView.LayoutManager create(RecyclerView view) {
                return new GridLayoutManager(view.getContext(), spanCount, orientation, reverse);
            }
        };
    }
}
