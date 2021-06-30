package com.eazy.uibase.view.list;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.BindingViewHolder>
    implements View.OnClickListener {

    private List<Object> mItems = new ArrayList<>();

    private ItemBinding mItemBinding;
    private Object mEmptyItem;
    private ItemBinding mEmptyItemBinding;

    private OnItemClickListener mOnItemClickListener;
    private OnViewBindingCreateListener mOnViewBindingCreateListener;

    public void setItemBinding(ItemBinding binding) {
        mItemBinding = binding;
    }

    public void setEmptyItem(Object item, ItemBinding binding) {
        mEmptyItem = item;
        mEmptyItemBinding = binding;
    }

    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isEmptyItem(0)) {
            ViewBinding binding = (mEmptyItemBinding == null ? mItemBinding : mEmptyItemBinding).createBinding(parent, viewType);
            return new BindingViewHolder(this, binding);
        }
        ViewBinding binding = mItemBinding.createBinding(parent, viewType);
        if (mOnViewBindingCreateListener != null) {
            mOnViewBindingCreateListener.onViewBindingCreated((RecyclerView) parent, binding);
        }
        return new BindingViewHolder(this, binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
        Object item = getItem(position);
        if (isEmptyItem(position)) {
            holder.bind(mEmptyItemBinding == null ? mItemBinding : mEmptyItemBinding, item, position);
            return;
        }
        holder.bind(mItemBinding, item, position);
    }

    @Override
    public int getItemCount() {
        int size = getRealItemCount();
        if (isEmptyItem(0))
            size = 1;
        return size;
    }

    public Object getItem(int position) {
        if (isEmptyItem(position))
            return mEmptyItem;
        return getRealItem(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isEmptyItem(position))
            return (mEmptyItemBinding == null ? mItemBinding : mEmptyItemBinding).getItemViewType(mEmptyItem);
        return mItemBinding.getItemViewType(getItem(position));
    }

    public List<?> getData() {
        return mItems;
    }

    public void addAll(List<?> items) {
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

    public void replace(List<?> items) {
        mItems.clear();
        if (items != null && !items.isEmpty()) {
            mItems.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void replace(Iterable<?> items) {
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

    public void setOnViewBindingCreateListener(OnViewBindingCreateListener l) {
        this.mOnViewBindingCreateListener = l;
    }

    public int findPosition(Object item) {
        return mItems.indexOf(item);
    }

    public void adopt(RecyclerViewAdapter old) {
        mItems = old.mItems;
        mItemBinding = old.mItemBinding;
        mOnItemClickListener = old.mOnItemClickListener;
    }

    @Override
    public void onClick(View v) {
        RecyclerView recyclerView = (RecyclerView) v.getParent();
        int position = recyclerView.getChildAdapterPosition(v);
        if (mOnItemClickListener != null && position >= 0 && !mItems.isEmpty())
            mOnItemClickListener.onItemClick(recyclerView, v, position, getItem(position));
    }

    protected static class BindingViewHolder extends RecyclerView.ViewHolder {

        private final ViewBinding mBinding;

        public BindingViewHolder(View.OnClickListener listener, @NonNull ViewBinding binding) {
            super(binding.getRoot());
            if (listener != null)
                binding.getRoot().setOnClickListener(listener);
            mBinding = binding;
        }

        public void bind(ItemBinding binding, Object item, int position) {
            binding.bindView(mBinding, item, position);
            //mBinding.executePendingBindings();
        }
    }

    @FunctionalInterface
    public interface OnItemClickListener {
        void onItemClick(RecyclerView recyclerView, View view, int position, Object object);
    }

    @FunctionalInterface
    public interface OnViewBindingCreateListener {
        void onViewBindingCreated(RecyclerView recyclerView, ViewBinding view);
    }

    protected int getRealItemCount() {
        return mItems.size();
    }

    protected Object getRealItem(int position) {
        return mItems.get(position);
    }

    protected boolean isEmptyItem(int position) {
        return position == 0 && mItems.isEmpty() && (mEmptyItem != null || mEmptyItemBinding != null);
    }

}
