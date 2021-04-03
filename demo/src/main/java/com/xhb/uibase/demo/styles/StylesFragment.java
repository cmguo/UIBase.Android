package com.xhb.uibase.demo.styles;

import androidx.databinding.Bindable;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.xhb.uibase.demo.R;
import com.xhb.uibase.demo.core.ComponentFragment;
import com.xhb.uibase.demo.core.ViewModel;
import com.xhb.uibase.demo.core.ViewStyles;
import com.xhb.uibase.demo.databinding.Styles2FragmentBinding;
import com.xhb.uibase.view.list.PaddingDecoration;
import com.xhb.uibase.view.list.UnitTypeItemBinding;

import java.util.Map;

public class StylesFragment extends ComponentFragment<Styles2FragmentBinding, StylesFragment.Model, StylesFragment.Styles> {

    private static final String TAG = "StylesFragment";

    public static class Model extends ViewModel {
        private final Map<String, Integer> styles;

        public Model(StylesFragment fragment) {
            if (fragment.getComponent().id() == R.id.component_buttons)
                styles = com.xhb.uibase.demo.core.Styles.buttonStyles(fragment.getContext());
            else if (fragment.getComponent().id() == R.id.component_check_boxes)
                styles = com.xhb.uibase.demo.core.Styles.checkboxStyles(fragment.getContext());
            else if (fragment.getComponent().id() == R.id.component_radio_buttons)
                styles = com.xhb.uibase.demo.core.Styles.radioStyles(fragment.getContext());
            else if (fragment.getComponent().id() == R.id.component_switches)
                styles = com.xhb.uibase.demo.core.Styles.switchStyles(fragment.getContext());
            else
                styles = null;
        }

        public Map<String, Integer> getStyles() {
            return styles;
        }
    }

    public static class Styles extends ViewStyles {
        public ItemBinding itemBinding;
        public RecyclerView.ItemDecoration itemDecoration = new PaddingDecoration();

        @Bindable
        public String text = "文字";

        @Bindable
        public boolean disabled = false;

        private final StylesFragment fragment_;

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
            itemBinding = new ItemBinding(this);
        }
    }

    public static class ItemBinding extends UnitTypeItemBinding<Map.Entry<String, Integer>> {
        private final Styles styles;

        public ItemBinding(Styles styles) {
            super(styles.itemBindingId());
            this.styles = styles;
        }

        @Override
        public void bindView(ViewDataBinding binding, Map.Entry<String, Integer> item, int position) {
            super.bindView(binding, item, position);
            binding.setVariable(BR.styles, styles);
        }
    }

}
