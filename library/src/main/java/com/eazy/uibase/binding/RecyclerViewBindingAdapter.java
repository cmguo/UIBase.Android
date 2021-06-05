package com.eazy.uibase.binding;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eazy.uibase.view.list.ItemBindings;
import com.eazy.uibase.view.list.LayoutManagerFactory;
import com.eazy.uibase.view.list.RecyclerViewAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * RecyclerView 的 databinding adapter
 */
public class RecyclerViewBindingAdapter {

    @BindingAdapter("adapter")
    public static  void setRecyclerViewAdapter(RecyclerView recyclerView, RecyclerViewAdapter adapter) {
        RecyclerView.Adapter<?> old = recyclerView.getAdapter();
        if (old instanceof RecyclerViewAdapter) {
            adapter.adopt(((RecyclerViewAdapter) old));
        }
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("data")
    public static <LT> void setRecyclerViewData(RecyclerView recyclerView, LT data) {
        RecyclerViewAdapter adapter = getAdapter(recyclerView);
        if (adapter != null) {
            if (data instanceof List) {
                adapter.replace((List<?>) data);
            } else if (data instanceof Map) {
                adapter.replace(((Map<?, ?>) data).entrySet());
            } else if (data instanceof Iterable) {
                adapter.replace((Iterable<?>) data);
            } else if (data != null && data.getClass().isArray()) {
                adapter.replace(Arrays.asList((Object[]) data));
            }
        }
    }

    @BindingAdapter("itemBinding")
    public static <B> void setRecyclerViewItemBinding(RecyclerView recyclerView, B binding) {
        RecyclerViewAdapter adapter = getAdapter(recyclerView);
        if (adapter != null) {
            adapter.setItemBinding(ItemBindings.get(recyclerView.getContext(), binding));
        }
    }

    @BindingAdapter("itemClicked")
    public static  void setRecyclerViewOnItemClickListener(RecyclerView recyclerView,
                                                              RecyclerViewAdapter.OnItemClickListener listener) {
        RecyclerViewAdapter adapter = getAdapter(recyclerView);
        if (adapter != null) {
            adapter.setOnItemClickListener(listener);
        }
    }

    @BindingAdapter(value = {"layoutManager", "layoutManagerFactory"}, requireAll = false)
    public static void setRecyclerViewLayoutManager(RecyclerView view, RecyclerView.LayoutManager manager, LayoutManagerFactory factory) {
        if (manager != null)
            view.setLayoutManager(manager);
        else if (factory != null)
            view.setLayoutManager(factory.create(view));
    }

    @BindingAdapter("itemDecoration")
    public static void setRecyclerViewItemDecoration(RecyclerView view, RecyclerView.ItemDecoration decoration) {
        view.addItemDecoration(decoration);
    }

    @BindingAdapter("hasFixedSize")
    public static void setRecyclerViewHasFixedSize(RecyclerView view, boolean hasFixedSize) {
        view.setHasFixedSize(hasFixedSize);
    }

    private static  RecyclerViewAdapter getAdapter(RecyclerView recyclerView) {
        RecyclerView.Adapter<?> adapter = recyclerView.getAdapter();
        if (adapter == null) {
            adapter = new RecyclerViewAdapter();
            recyclerView.setAdapter(adapter);
        }
        if (recyclerView.getLayoutManager() == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        }
        return adapter instanceof RecyclerViewAdapter ? (RecyclerViewAdapter) adapter : null;
    }

}
