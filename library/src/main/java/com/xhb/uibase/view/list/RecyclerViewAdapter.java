package com.xhb.uibase.view.list;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewAdapter.BindingViewHolder<T>>
    implements View.OnClickListener {

    private List<T> mItems = new ArrayList<>();

    private ItemBinding<T> mItemBinding;

    private OnItemClickListener<T> mOnItemClickListener;

    public void setItemBinding(ItemBinding<T> binding) {
        mItemBinding = binding;
    }

    @NonNull
    @Override
    public BindingViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewBinding binding = mItemBinding.createBinding(parent, viewType);
        return new BindingViewHolder<>(this, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder<T> holder, int position) {
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

    public void setOnItemClickListener(OnItemClickListener<T> l) {
        this.mOnItemClickListener = l;
    }

    public int findPosition(T item) {
        return mItems.indexOf(item);
    }

    public void adopt(RecyclerViewAdapter<T> old) {
        mItems = old.mItems;
        mItemBinding = old.mItemBinding;
        mOnItemClickListener = old.mOnItemClickListener;
    }

    @Override
    public void onClick(View v) {
        int position = ((RecyclerView) v.getParent()).getChildAdapterPosition(v);
        if (mOnItemClickListener != null && position >= 0)
            mOnItemClickListener.onItemClick(position, mItems.get(position));
    }

    protected static class BindingViewHolder<T> extends RecyclerView.ViewHolder {

        private final ViewBinding mBinding;

        public BindingViewHolder(View.OnClickListener listener, @NonNull ViewBinding binding) {
            super(binding.getRoot());
            if (listener != null)
                binding.getRoot().setOnClickListener(listener);
            mBinding = binding;
        }

        public void bind(ItemBinding<T> binding, T item, int position) {
            binding.bindView(mBinding, item, position);
            //mBinding.executePendingBindings();
        }
    }

    @FunctionalInterface
    public interface OnItemClickListener<T> {
        void onItemClick(int position, T object);
    }

}
