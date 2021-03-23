package com.eazy.uibase.binding;

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

import com.eazy.uibase.BR;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RecyclerView 的 databinding adapter
 */
public class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewAdapter.BindingViewHolder> {

    @BindingAdapter("adapter")
    public static void setRecyclerViewAdapter(RecyclerView recyclerView, RecyclerViewAdapter adapter) {
        RecyclerView.Adapter old = recyclerView.getAdapter();
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
                adapter.replace((List) data);
            } else if (data instanceof Map) {
                adapter.replace(((Map) data).entrySet());
            } else if (data instanceof Iterable) {
                adapter.replace((Iterable) data);
            }
        }
    }

    @BindingAdapter("itemBinding")
    public static <T> void setRecyclerViewItemBinding(RecyclerView recyclerView, T binding) {
        RecyclerViewAdapter adapter = getAdapter(recyclerView);
        if (adapter != null) {
            if (binding instanceof Integer)
                adapter.setItemBinding(
                        new UnitTypeItemBinding(recyclerView.getContext(), (Integer) binding));
            else if (binding instanceof RecyclerViewAdapter.ItemBinding)
                adapter.setItemBinding((ItemBinding) binding);
        }
    }

    @BindingAdapter("itemClicked")
    public static <T> void setRecyclerViewOnItemClickListener(RecyclerView recyclerView, OnItemClickListener listener) {
        RecyclerViewAdapter adapter = getAdapter(recyclerView);
        if (adapter != null) {
            OnItemClickListener old = adapter.setOnItemClickListener(listener);
            if ((old == null) == (listener == null))
                return;
            for (int i = 0, n = recyclerView.getChildCount(); i < n; ++i) {
                RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                if (holder instanceof BindingViewHolder) {
                    ((BindingViewHolder) holder).updateListener();
                }
            }
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

    public interface ItemBinding<T> {
        int getItemViewType(T item);

        ViewDataBinding createBinding(@NonNull ViewGroup parent, int viewType);

        void bindView(ViewDataBinding binding, T item, int position);
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

    public static class UnitTypeItemBinding<T> extends BaseItemLayout<T> {

        private int mItemLayout;

        public UnitTypeItemBinding(Context context) {
            super(context);
        }

        public UnitTypeItemBinding(int itemLayout) {
            mItemLayout = itemLayout;
        }

        public UnitTypeItemBinding(Context context, int itemLayout) {
            super(context);
            mItemLayout = itemLayout;
        }

        @Override
        public int getItemViewType(T item) {
            return mItemLayout;
        }
    }

    public abstract static class BaseItemLayout<T> implements ItemBinding<T> {

        private LayoutInflater mInflater;
        private RecyclerView.Adapter mAdapter;

        public BaseItemLayout() {
        }

        public BaseItemLayout(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public ViewDataBinding createBinding(@NonNull ViewGroup parent, int viewType) {
            if (mInflater == null)
                mInflater = LayoutInflater.from(parent.getContext());
            return DataBindingUtil.inflate(mInflater, viewType, parent, false);
        }

        @Override
        @CallSuper
        public void bindView(ViewDataBinding binding, T item, int position) {
            binding.setVariable(BR.data, item);
        }

        public void setAdapter(RecyclerView.Adapter adapter) {
            this.mAdapter = adapter;
        }

        protected RecyclerView.Adapter getAdpater() {
            return mAdapter;
        }
    }


    private List<T> mItems = new ArrayList<>();

    private ItemBinding mItemBinding;

    private OnItemClickListener mOnItemClickListener;

    public void setItemBinding(ItemBinding binding) {
        mItemBinding = binding;
        if (binding instanceof RecyclerViewAdapter.UnitTypeItemBinding)
            ((UnitTypeItemBinding) mItemBinding).setAdapter(this);
    }

    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = mItemBinding.createBinding(parent, viewType);
        return new BindingViewHolder(this, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
        T item = mItems.get(position);
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

    public List<T> getData() {
        return mItems;
    }

    public void addAll(List<? extends T> items) {
        if (items == null || items.isEmpty()) return;
        int size = mItems.size();
        mItems.addAll(items);
        notifyItemRangeInserted(size, items.size());
    }

    public void add(T item) {
        if (item == null) return;
        int size = mItems.size();
        mItems.add(item);
        notifyItemInserted(size);
    }

    public void remove(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    public void remove(T item) {
        int position = findPosition(item);
        if (position < 0) return;
        remove(position);
    }

    public void replace(List<? extends T> items) {
        mItems.clear();
        if (items != null && !items.isEmpty()) {
            mItems.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void replace(Iterable<? extends T> items) {
        mItems.clear();
        if (items != null) {
            for (T i : items)
                mItems.add(i);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public OnItemClickListener setOnItemClickListener(OnItemClickListener l) {
        OnItemClickListener old = mOnItemClickListener;
        this.mOnItemClickListener = l;
        return old;
    }

    public int findPosition(T item) {
        return mItems.indexOf(item);
    }

    private void adopt(RecyclerViewAdapter old) {
        mItems = old.mItems;
        mItemBinding = old.mItemBinding;
        mOnItemClickListener = old.mOnItemClickListener;
    }

    protected static class BindingViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RecyclerViewAdapter mAdapter;
        private ViewDataBinding mBinding;
        private int mPosition;
        private T mItem;

        public BindingViewHolder(@NonNull RecyclerViewAdapter adapter, @NonNull ViewDataBinding binding) {
            super(binding.getRoot());
            mAdapter = adapter;
            if (mAdapter.mOnItemClickListener != null)
                binding.getRoot().setOnClickListener(this);
            mBinding = binding;
        }

        void updateListener() {
            if (mAdapter.mOnItemClickListener != null)
                mBinding.getRoot().setOnClickListener(this);
            else
                mBinding.getRoot().setOnClickListener(null);
        }

        @Override
        public void onClick(View v) {
            if (mAdapter.mOnItemClickListener != null) {
                mAdapter.mOnItemClickListener.onItemClick(mPosition, mItem);
            }
        }

        public void bind(ItemBinding binding, T item, int position) {
            mPosition = position;
            mItem = item;
            binding.bindView(mBinding, item, position);
            //mBinding.executePendingBindings();
        }

        public void setItemPosition(int orig) {
            mPosition = orig;
        }
    }

    @FunctionalInterface
    public interface OnItemClickListener<T> {
        void onItemClick(int position, T object);
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

    public interface LayoutManagerFactory {
        RecyclerView.LayoutManager create(RecyclerView view);
    }

}
