package com.eazy.uibase.app.ui.main;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eazy.uibase.app.R;
import com.eazy.uibase.app.databinding.StylesFragmentBinding;
import com.eazy.uibase.binding.RecyclerViewAdapter;
import com.eazy.uibase.demo.core.FragmentComponent;
import com.eazy.uibase.demo.view.recycler.PaddingDecoration;

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
            ViewDataBinding binding = super.createBinding(parent, R.layout.style_item);
            ViewGroup group = (ViewGroup) binding.getRoot().findViewById(R.id.value);
            ViewDataBinding valueBinding = super.createBinding(group, viewType);
            group.addView(valueBinding.getRoot());
            return binding;
        }

        @Override
        public void bindView(ViewDataBinding binding, StylesViewModel.StyleValue item, int position) {
            super.bindView(binding, item, position);
            ViewGroup group = (ViewGroup) binding.getRoot().findViewById(R.id.value);
            ViewDataBinding valueBinding = DataBindingUtil.findBinding(group.getChildAt(0));
            valueBinding.setVariable(BR.value, item);
        }
    }

    public static StylesFragment newInstance() {
        return new StylesFragment();
    }

    private StylesViewModel mViewModel;

    public StyleItemLayout itemLayout = new StyleItemLayout();
    public RecyclerView.ItemDecoration itemDecoration = new PaddingDecoration();

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
        if (component != null)
            mViewModel.bindComponent(component);
        DataBindingUtil.findBinding(getView()).setVariable(BR.model, mViewModel);
    }

    private FragmentComponent component;

    public void bindComponent(FragmentComponent component) {
        if (mViewModel != null)
            mViewModel.bindComponent(component);
        else
            this.component = component;
    }

}