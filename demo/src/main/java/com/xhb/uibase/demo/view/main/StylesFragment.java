package com.xhb.uibase.demo.view.main;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xhb.uibase.demo.R;
import com.xhb.uibase.demo.databinding.StyleItemBinding;
import com.xhb.uibase.demo.databinding.StylesFragmentBinding;
import com.xhb.uibase.binding.RecyclerViewAdapter;
import com.xhb.uibase.demo.core.ComponentFragment;
import com.xhb.uibase.demo.view.recycler.DeviderDecoration;
import com.xhb.uibase.demo.view.recycler.PaddingDecoration;

public class StylesFragment extends Fragment {

    public static class StyleItemLayout extends RecyclerViewAdapter.BaseItemLayout<StylesViewModel.StyleValue> {

        public StyleItemLayout() {
        }

        @Override
        public int getItemViewType(StylesViewModel.StyleValue item) {
            Class type = item.style.getValueType();
            if (item.style.getValues() != null)
                return R.layout.style_value_list;
            if (type == Boolean.TYPE)
                return R.layout.style_value_bool;
            return R.layout.style_value_text;
        }

        @Override
        public ViewDataBinding createBinding(@NonNull ViewGroup parent, int viewType) {
            if (viewType == R.layout.style_desc) {
                return super.createBinding(parent, viewType);
            }
            ViewDataBinding binding = super.createBinding(parent, R.layout.style_item);
            ViewGroup group = (ViewGroup) binding.getRoot().findViewById(R.id.value);
            ViewDataBinding valueBinding = super.createBinding(group, viewType);
            group.addView(valueBinding.getRoot());
            return binding;
        }

        @Override
        public void bindView(ViewDataBinding binding, StylesViewModel.StyleValue item, int position) {
            super.bindView(binding, item, position);
            if (binding instanceof StyleItemBinding) {
                ViewGroup group = (ViewGroup) binding.getRoot().findViewById(R.id.value);
                ViewDataBinding valueBinding = DataBindingUtil.findBinding(group.getChildAt(0));
                valueBinding.setVariable(BR.value, item);
            }
        }
    }

    public static class StylesAdapter extends RecyclerViewAdapter {

        private int expandSyle = 0;

        public void toggle(int position) {
            if (expandSyle > 0) {
                if (expandSyle == position)
                    return;
                if (position > expandSyle)
                    --position;
            }
            if (position == expandSyle - 1) {
                expandSyle = 0;
            } else {
                expandSyle = position + 1;
            }
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return super.getItemCount() + (expandSyle > 0 ? 1 : 0);
        }

        @Override
        public int getItemViewType(int position) {
            if (expandSyle > 0 && position == expandSyle)
                return R.layout.style_desc;
            if (expandSyle > 0 && position > expandSyle)
                --position;
            return super.getItemViewType(position);
        }

        @Override
        public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
            int orig = position;
            if (expandSyle > 0 && position >= expandSyle)
                --position;
            super.onBindViewHolder(holder, position);
            holder.setItemPosition(orig);
        }
    }

    public static StylesFragment newInstance() {
        return new StylesFragment();
    }

    private StylesViewModel mViewModel;
    private ComponentFragment fragment;

    public StylesAdapter adapter = new StylesAdapter();
    public StyleItemLayout itemLayout = new StyleItemLayout();
    public RecyclerView.ItemDecoration itemDecoration = new DeviderDecoration(getContext());
    public RecyclerViewAdapter.OnItemClickListener itemClicked = new RecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, Object object) {
            adapter.toggle(position);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        StylesFragmentBinding binding = StylesFragmentBinding.inflate(inflater);
        binding.setLifecycleOwner(this);
        binding.setFragment(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StylesViewModel.class);
        if (fragment != null)
            mViewModel.bindComponent(fragment);
        DataBindingUtil.findBinding(getView()).setVariable(BR.model, mViewModel);
    }

    public void bindComponent(ComponentFragment fragment) {
        if (mViewModel != null)
            mViewModel.bindComponent(fragment);
        else
            this.fragment = fragment;
    }

}