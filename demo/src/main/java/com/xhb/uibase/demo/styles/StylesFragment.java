package com.xhb.uibase.demo.styles;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.xhb.uibase.binding.RecyclerViewAdapter;
import com.xhb.uibase.demo.R;
import com.xhb.uibase.demo.core.ComponentFragment;
import com.xhb.uibase.demo.core.SkinManager;
import com.xhb.uibase.demo.core.Styles;
import com.xhb.uibase.demo.core.ViewModel;
import com.xhb.uibase.demo.core.ViewStyles;
import com.xhb.uibase.demo.core.annotation.Title;
import com.xhb.uibase.demo.databinding.Styles2FragmentBinding;
import com.xhb.uibase.demo.view.recycler.PaddingDecoration;

import java.util.Map;

import skin.support.observe.SkinObservable;
import skin.support.observe.SkinObserver;

public class StylesFragment extends ComponentFragment<Styles2FragmentBinding, StylesFragment.Model, StylesFragment.Style>
        implements SkinObserver {

    private static final String TAG = "StylesFragment";

    public static class Model extends ViewModel {
        private Map<String, Integer> styles;

        public Model(StylesFragment fragment) {
            if (fragment.getComponent().id() == R.id.component_buttons)
                styles = Styles.buttonStyles(fragment.getContext());
            else if (fragment.getComponent().id() == R.id.component_check_boxes)
                styles = Styles.checkboxStyles(fragment.getContext());
            else if (fragment.getComponent().id() == R.id.component_radio_buttons)
                styles = Styles.radioStyles(fragment.getContext());
            else if (fragment.getComponent().id() == R.id.component_switches)
                styles = Styles.switchStyles(fragment.getContext());
            else
                styles = null;
        }

        public Map<String, Integer> getStyles() {
            return styles;
        }
    }

    public static class Style extends ViewStyles {
        public ItemLayout itemLayout;
        public RecyclerView.ItemDecoration itemDecoration = new PaddingDecoration();

        @Bindable @Title("文字")
        public String text = "文字";

        @Bindable @Title("禁用")
        public boolean disabled = false;

        private StylesFragment fragment_;

        public int itemLayoutId() {
            if (fragment_.getComponent().id() == R.id.component_buttons)
                return R.layout.style_button_item;
            else if (fragment_.getComponent().id() == R.id.component_check_boxes)
                return R.layout.style_check_box_item;
            else if (fragment_.getComponent().id() == R.id.component_radio_buttons)
                return R.layout.style_radio_button_item;
            else if (fragment_.getComponent().id() == R.id.component_switches)
                return R.layout.style_switch_item;
            else
                return 0;
        }

        public Style(StylesFragment fragment) {
            this.fragment_ = fragment;
            itemLayout = new ItemLayout(this);
        }
    }

    public static class ItemLayout extends RecyclerViewAdapter.UnitTypeItemLayout<Map.Entry<String, Integer>> {
        private final Style style;

        public ItemLayout(Style style) {
            super(style.itemLayoutId());
            this.style = style;
        }

        @Override
        public void bindView(ViewDataBinding binding, Map.Entry<String, Integer> item, int position) {
            super.bindView(binding, item, position);
            binding.setVariable(BR.style, style);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SkinManager.addObserver(this);
    }

    @Override
    public void onDestroy() {
        SkinManager.removeObserver(this);
        super.onDestroy();
    }

    @Override
    public void updateSkin(SkinObservable observable, Object o) {
        getBinding().buttonsList.getAdapter().notifyItemRangeChanged(0, getModel().styles.size());
    }
}
