package com.eazy.uibase.demo.resources;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.graphics.Color;

import androidx.annotation.StyleRes;
import androidx.databinding.Bindable;
import androidx.databinding.ViewDataBinding;

import com.eazy.uibase.demo.BR;
import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.ComponentFragment;
import com.eazy.uibase.demo.core.ViewModel;
import com.eazy.uibase.demo.core.ViewStyles;
import com.eazy.uibase.demo.core.style.annotation.Description;
import com.eazy.uibase.demo.core.style.annotation.Title;
import com.eazy.uibase.demo.databinding.Styles2FragmentBinding;
import com.eazy.uibase.view.list.ItemDecorations;
import com.eazy.uibase.view.list.UnitTypeItemBinding;

import java.util.Map;

public class StylesFragment extends ComponentFragment<Styles2FragmentBinding, StylesFragment.Model, StylesFragment.Styles> {

    private static final String TAG = "StylesFragment";

    public static class Model extends ViewModel {
        private final Map<String, Integer> styles;

        public Model(StylesFragment fragment) {
            if (fragment.getComponent().id() == R.id.component_buttons)
                styles = com.eazy.uibase.demo.resources.Styles.buttonStyles();
            else if (fragment.getComponent().id() == R.id.component_check_boxes)
                styles = com.eazy.uibase.demo.resources.Styles.checkboxStyles();
            else if (fragment.getComponent().id() == R.id.component_radio_buttons)
                styles = com.eazy.uibase.demo.resources.Styles.radioStyles();
            else if (fragment.getComponent().id() == R.id.component_switches)
                styles = com.eazy.uibase.demo.resources.Styles.switchStyles();
            else if (fragment.getComponent().id() == R.id.component_text_appearances)
                styles = com.eazy.uibase.demo.resources.Styles.textAppearances();
            else
                styles = null;
        }

        public Map<String, Integer> getStyles() {
            return styles;
        }
    }

    public static class Styles extends ViewStyles {
        public final ItemBinding itemBinding;
        public final ItemDecorations.Builder itemDecoration;

        @Bindable
        public String text = "文字";

        public String textTitle(String title) {
            return "文字样式： " + title;
        }

        @SuppressLint("DefaultLocale")
        public String textDetail(@StyleRes int id) {
            TypedArray a = fragment_.requireContext().obtainStyledAttributes(id, R.styleable.ZTextAppearance);
            float size = a.getDimension(R.styleable.ZTextAppearance_android_textSize, 0);
            float lineHeight = a.getDimension(R.styleable.ZTextAppearance_lineHeight, 0);
            int color = a.getResourceId(R.styleable.ZTextAppearance_android_textColor, 0);
            a.recycle();
            String colorStr = Resources.simpleName(fragment_.requireContext().getResources().getResourceName(color));
            return String.format("size: %d, lineHeight: %d, color: %s", (int)size, (int)lineHeight, colorStr);
        }

        public String textAll(Map.Entry<String, Integer> data) {
            return textTitle(data.getKey()) + "\n" + textDetail(data.getValue());
        }

        @Bindable
        @Title("禁用")
        @Description("切换到禁用状态")
        public boolean disabled = false;

        @Bindable
        @Title("文字背景")
        @Description("文字背景，适用文字样式")
        public boolean textBackgroud = false;

        @Bindable
        @Title("文字居中")
        @Description("文字居中，适用文字样式")
        public boolean textAlignmentCenter = false;

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
            else if (fragment_.getComponent().id() == R.id.component_text_appearances)
                return R.layout.style_text_appearance_item;
            else
                return 0;
        }

        public Styles(StylesFragment fragment) {
            this.fragment_ = fragment;
            itemBinding = new ItemBinding(this);
            itemDecoration = ItemDecorations.divider(1f, Color.BLUE);
        }
    }

    public static class ItemBinding extends UnitTypeItemBinding {
        private final Styles styles;

        public ItemBinding(Styles styles) {
            super(styles.itemBindingId());
            this.styles = styles;
        }

        @Override
        public void bindView(ViewDataBinding binding, Object item, int position) {
            super.bindView(binding, item, position);
            binding.setVariable(BR.styles, styles);
        }
    }

}
