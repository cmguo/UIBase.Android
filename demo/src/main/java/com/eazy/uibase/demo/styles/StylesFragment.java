package com.eazy.uibase.demo.styles;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.ComponentFragment;
import com.eazy.uibase.demo.core.SkinManager;
import com.eazy.uibase.demo.core.ViewModel;
import com.eazy.uibase.demo.core.ViewStyles;
import com.eazy.uibase.demo.databinding.Styles2FragmentBinding;
import com.eazy.uibase.view.list.PaddingDecoration;
import com.eazy.uibase.view.list.UnitTypeItemBinding;

import java.util.Map;

import skin.support.observe.SkinObservable;
import skin.support.observe.SkinObserver;

public class StylesFragment extends ComponentFragment<Styles2FragmentBinding, StylesFragment.Model, StylesFragment.Styles>
        implements SkinObserver {

    private static final String TAG = "StylesFragment";

    public static class Model extends ViewModel {
        private Map<String, Integer> styles;

        public Model(StylesFragment fragment) {
            if (fragment.getComponent().id() == R.id.component_buttons)
                styles = com.eazy.uibase.demo.core.Styles.buttonStyles(fragment.getContext());
            else if (fragment.getComponent().id() == R.id.component_check_boxes)
                styles = com.eazy.uibase.demo.core.Styles.checkboxStyles(fragment.getContext());
            else if (fragment.getComponent().id() == R.id.component_radio_buttons)
                styles = com.eazy.uibase.demo.core.Styles.radioStyles(fragment.getContext());
            else if (fragment.getComponent().id() == R.id.component_switches)
                styles = com.eazy.uibase.demo.core.Styles.switchStyles(fragment.getContext());
            else
                styles = null;
        }

        public Map<String, Integer> getStyles() {
            return styles;
        }
    }

    public static class Styles extends ViewStyles {
        public ItemLayout itemBinding;
        public RecyclerView.ItemDecoration itemDecoration = new PaddingDecoration();

        @Bindable
        public String text = "文字";

        @Bindable
        public boolean disabled = false;

        private StylesFragment fragment_;

        public int itemBindingId() {
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

        public Styles(StylesFragment fragment) {
            this.fragment_ = fragment;
            itemBinding = new ItemLayout(this);
        }
    }

    public static class ItemLayout extends UnitTypeItemBinding<Map.Entry<String, Integer>> {
        private final Styles styles;

        public ItemLayout(Styles styles) {
            super(styles.itemBindingId());
            this.styles = styles;
        }

        @Override
        public void bindView(ViewDataBinding binding, Map.Entry<String, Integer> item, int position) {
            super.bindView(binding, item, position);
            binding.setVariable(BR.styles, styles);
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
