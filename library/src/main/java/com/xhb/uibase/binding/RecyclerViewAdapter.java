package com.xhb.uibase.binding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xhb.uibase.BR;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RecyclerView 的 databinding adapter
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.BindingViewHolder> {

    @androidx.databinding.BindingAdapter("data")
    public static <T> void setRecyclerViewData(RecyclerView recyclerView, T data) {
        RecyclerViewAdapter adapter = getAdapter(recyclerView);
        if (adapter != null) {
            if (data instanceof List) {
                adapter.replace((List) data);
            } else if (data instanceof Map) {
                adapter.replace(((Map) data).entrySet());
            } else if (data instanceof Iterable) {
                adapter.replace((Iterable) data);
            }
        }
    }

    @androidx.databinding.BindingAdapter("itemLayout")
    public static <T> void setRecyclerViewTemplate(RecyclerView recyclerView, T template) {
        RecyclerViewAdapter adapter = getAdapter(recyclerView);
        if (adapter != null) {
            if (template instanceof Integer)
                adapter.setItemLayout(
                        new BaseItemLayout(recyclerView.getContext(), (Integer) template));
            else if (template instanceof ItemLayout)
                adapter.setItemLayout((ItemLayout) template);
        }
    }

    @androidx.databinding.BindingAdapter("itemClicked")
    public static <T> void setOnItemClickListener(RecyclerView recyclerView, OnItemClickListener listener) {
        RecyclerViewAdapter adapter = getAdapter(recyclerView);
        if (adapter != null) {
            adapter.setOnItemClickListener(listener);
        }
    }

    @BindingAdapter(value = {"layoutManager", "adapter"}, requireAll = false)
    public static void setRecyclerLayoutManager(RecyclerView view, LayoutManagerFactory factory, RecyclerView.Adapter adapter) {
        if (factory != null)
            view.setLayoutManager(factory.create(view));
        if (adapter != null)
            view.setAdapter(adapter);
    }

    @BindingAdapter("itemDecoration")
    public static void setRecyclerItem(RecyclerView view, RecyclerView.ItemDecoration decoration) {
        view.addItemDecoration(decoration);
    }

    @BindingAdapter("hasFixedSize")
    public static void setRecyclerHasFixedSize(RecyclerView view, boolean hasFixedSize) {
        view.setHasFixedSize(hasFixedSize);
    }

    public interface ItemLayout {
        int getItemViewType(Object item);

        ViewDataBinding createBinding(@NonNull ViewGroup parent, int viewType);

        void bindView(ViewDataBinding binding, Object item, int position);
    }

    private static RecyclerViewAdapter getAdapter(RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) {
            adapter = new RecyclerViewAdapter();
            recyclerView.setAdapter(adapter);
        }
        if (recyclerView.getLayoutManager() == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        }
        return adapter instanceof RecyclerViewAdapter ? (RecyclerViewAdapter) adapter : null;
    }

    public static class BaseItemLayout implements ItemLayout {

        private LayoutInflater mInflater;
        private int mItemLayout;
        private RecyclerView.Adapter mAdapter;

        public BaseItemLayout() {
        }

        public BaseItemLayout(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        public BaseItemLayout(int itemLayout) {
            mItemLayout = itemLayout;
        }

        public BaseItemLayout(Context context, int itemLayout) {
            this(context);
            mItemLayout = itemLayout;
        }

        @Override
        public int getItemViewType(Object item) {
            return mItemLayout;
        }

        @Override
        public ViewDataBinding createBinding(@NonNull ViewGroup parent, int viewType) {
            if (mInflater == null)
                mInflater = LayoutInflater.from(parent.getContext());
            return DataBindingUtil.inflate(mInflater, viewType, parent, false);
        }

        @Override
        @CallSuper
        public void bindView(ViewDataBinding binding, Object item, int position) {
            binding.setVariable(BR.data, item);
        }

        protected RecyclerView.Adapter getAdpater() {
            return mAdapter;
        }
    }


    private List<Object> mItems = new ArrayList<>();

    private ItemLayout mItemBinding;

    private OnItemClickListener mOnItemClickListener;

    public void setItemLayout(ItemLayout binding) {
        mItemBinding = binding;
        if (binding instanceof BaseItemLayout)
            ((BaseItemLayout) mItemBinding).mAdapter = this;
    }

    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = mItemBinding.createBinding(parent, viewType);
        return new BindingViewHolder(binding, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
        Object item = mItems.get(position);
        holder.bind(mItemBinding, item, position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItemBinding.getItemViewType(mItems.get(position));
    }

    public List<Object> getData() {
        return mItems;
    }

    public void addAll(List<? extends Object> items) {
        if (items == null || items.isEmpty()) return;
        int size = mItems.size();
        mItems.addAll(items);
        notifyItemRangeInserted(size, items.size());
    }

    public void add(Object item) {
        if (item == null) return;
        int size = mItems.size();
        mItems.add(item);
        notifyItemInserted(size);
    }

    public void remove(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    public void remove(Object item) {
        int position = findPosition(item);
        if (position < 0) return;
        remove(position);
    }

    public void replace(List<? extends Object> items) {
        mItems.clear();
        if (items != null && !items.isEmpty()) {
            mItems.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void replace(Iterable<? extends Object> items) {
        mItems.clear();
        if (items != null) {
            for (Object i : items)
                mItems.add(i);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.mOnItemClickListener = l;
    }

    public int findPosition(Object item) {
        return mItems.indexOf(item);
    }

    public static class BindingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ViewDataBinding mBinding;
        private OnItemClickListener mOnItemClickListener;
        private int mPosition;
        private Object mItem;

        public BindingViewHolder(@NonNull ViewDataBinding binding, OnItemClickListener l) {
            super(binding.getRoot());
            mOnItemClickListener = l;

            binding.getRoot().setOnClickListener(this);
            mBinding = binding;
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(mPosition, mItem);
            }
        }

        public void bind(ItemLayout binding, Object item, int position) {
            mPosition = position;
            mItem = item;
            binding.bindView(mBinding, item, position);
            //mBinding.executePendingBindings();
        }
    }

    @FunctionalInterface
    public interface OnItemClickListener {
        void onItemClick(int position, Object object);
    }

    public static class LayoutManagers {
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

    public static interface LayoutManagerFactory {
        RecyclerView.LayoutManager create(RecyclerView view);
    }

}
