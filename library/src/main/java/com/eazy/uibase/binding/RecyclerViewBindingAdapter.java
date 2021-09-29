package com.eazy.uibase.binding;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eazy.uibase.view.list.ItemBindings;
import com.eazy.uibase.view.list.ItemDecorations;
import com.eazy.uibase.view.list.Items;
import com.eazy.uibase.view.list.LayoutManagers;
import com.eazy.uibase.view.list.RecyclerViewAdapter;

import java.util.List;

/**
 * RecyclerView 的 databinding adapter
 */
public class RecyclerViewBindingAdapter {

    @BindingAdapter("adapter")
    public static  void setAdapter(RecyclerView recyclerView, RecyclerViewAdapter adapter) {
        RecyclerView.Adapter<?> old = recyclerView.getAdapter();
        if (old instanceof RecyclerViewAdapter) {
            adapter.adopt(((RecyclerViewAdapter) old));
        }
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("data")
    public static <LT> void setData(RecyclerView recyclerView, LT data) {
        RecyclerViewAdapter adapter = getAdapter(recyclerView);
        if (adapter != null) {
            adapter.setItems(Items.get(recyclerView.getContext(), data));
        }
    }

    @BindingAdapter("itemBinding")
    public static <B> void setItemBinding(RecyclerView recyclerView, B binding) {
        RecyclerViewAdapter adapter = getAdapter(recyclerView);
        if (adapter != null) {
            adapter.setItemBinding(ItemBindings.get(recyclerView.getContext(), binding));
        }
    }

    @BindingAdapter(value = {"emptyItem", "emptyItemBinding"}, requireAll = false)
    public static <I, B> void setEmptyItem(RecyclerView recyclerView, I item, B binding) {
        RecyclerViewAdapter adapter = getAdapter(recyclerView);
        if (adapter != null) {
            adapter.setEmptyItem(item, ItemBindings.get(recyclerView.getContext(), binding));
        }
    }

    @BindingAdapter("itemClicked")
    public static  void setOnItemClickListener(RecyclerView recyclerView,
                                                              RecyclerViewAdapter.OnItemClickListener listener) {
        RecyclerViewAdapter adapter = getAdapter(recyclerView);
        if (adapter != null) {
            adapter.setOnItemClickListener(listener);
        }
    }

    @BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView view, RecyclerView.LayoutManager manager) {
        if (manager != null)
            view.setLayoutManager(manager);
    }

    @BindingAdapter("layoutManager")
    public static void setLayoutManager(RecyclerView view, LayoutManagers.Builder factory) {
        if (factory != null)
            view.setLayoutManager(factory.build(view));
    }

    @BindingAdapter("itemDecoration")
    public static void setItemDecoration(RecyclerView view, RecyclerView.ItemDecoration decoration) {
        while (view.getItemDecorationCount() > 0)
            view.removeItemDecorationAt(0);
        if (decoration != null)
            view.addItemDecoration(decoration);
    }

    @BindingAdapter("itemDecoration")
    public static void setItemDecoration(RecyclerView view, ItemDecorations.Builder builder) {
        setItemDecoration(view, builder == null ? null : builder.build(view));
    }

    @BindingAdapter("hasFixedSize")
    public static void setHasFixedSize(RecyclerView view, boolean hasFixedSize) {
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
