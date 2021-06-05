package com.eazy.uibase.demo.view.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.ComponentFragment;
import com.eazy.uibase.demo.core.style.ColorStyle;
import com.eazy.uibase.demo.core.style.DimenStyle;
import com.eazy.uibase.demo.databinding.StylesFragmentBinding;
import com.eazy.uibase.view.list.BaseItemBinding;
import com.eazy.uibase.view.list.DividerDecoration;
import com.eazy.uibase.view.list.RecyclerViewAdapter;

public class StylesFragment extends Fragment {

    public static class StyleItemBinding extends BaseItemBinding {

        public StyleItemBinding() {
        }

        @Override
        public int getItemViewType(Object item) {
            StylesViewModel.StyleValue styleValue = (StylesViewModel.StyleValue) item;
            Class<?> type = styleValue.style.getValueType();
            if (styleValue.style.getValues() != null)
                return R.layout.style_value_list;
            if (type == Boolean.TYPE)
                return R.layout.style_value_bool;
            return R.layout.style_value_text;
        }

        @Override
        public ViewBinding createBinding(@NonNull ViewGroup parent, int viewType) {
            if (viewType == R.layout.style_desc) {
                return super.createBinding(parent, viewType);
            }
            ViewBinding binding = super.createBinding(parent, R.layout.style_item);
            ViewGroup group = binding.getRoot().findViewById(R.id.value);
            ViewBinding valueBinding = super.createBinding(group, viewType);
            group.addView(valueBinding.getRoot());
            return binding;
        }

        @Override
        public void bindView(ViewBinding binding, Object item, int position) {
            super.bindView(binding, item, position);
            if (binding instanceof com.eazy.uibase.demo.databinding.StyleItemBinding) {
                ViewGroup group = binding.getRoot().findViewById(R.id.value);
                ViewDataBinding valueBinding = DataBindingUtil.findBinding(group.getChildAt(0));
                valueBinding.setVariable(BR.value, item);
            }
        }
    }

    public static class StylesAdapter extends RecyclerViewAdapter {

        private int expandStyle = 0;

        public void toggle(int position) {
            if (expandStyle > 0) {
                if (expandStyle == position)
                    return;
                if (position > expandStyle)
                    --position;
            }
            if (position == expandStyle - 1) {
                expandStyle = 0;
            } else {
                expandStyle = position + 1;
            }
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return super.getItemCount() + (expandStyle > 0 ? 1 : 0);
        }

        @Override
        public Object getItem(int position) {
            if (expandStyle > 0 && position >= expandStyle)
                --position;
            return super.getItem(position);
        }

        @Override
        public int getItemViewType(int position) {
            if (expandStyle > 0 && position == expandStyle)
                return R.layout.style_desc;
            return super.getItemViewType(position);
        }

    }

    private StylesViewModel mViewModel;
    private ComponentFragment<?, ?, ?> fragment;

    public StylesAdapter adapter = new StylesAdapter();
    public StyleItemBinding itemBinding = new StyleItemBinding();
    public RecyclerView.ItemDecoration itemDecoration = new DividerDecoration(LinearLayout.VERTICAL, 1);
    public RecyclerViewAdapter.OnItemClickListener itemClicked = (rv, v, position, object) -> adapter.toggle(position);

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
        ColorStyle.init(requireContext());
        DimenStyle.init(requireContext());
        mViewModel = new ViewModelProvider(this).get(StylesViewModel.class);
        if (fragment != null)
            mViewModel.bindComponent(fragment);
        DataBindingUtil.findBinding(requireView()).setVariable(BR.model, mViewModel);
    }

    public void bindComponent(ComponentFragment<?, ?, ?> fragment) {
        if (mViewModel != null)
            mViewModel.bindComponent(fragment);
        else
            this.fragment = fragment;
    }

}