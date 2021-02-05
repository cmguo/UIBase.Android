package com.xhb.uibase.demo.buttons;

import android.util.Log;

import androidx.databinding.Bindable;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.google.auto.service.AutoService;
import com.xhb.uibase.binding.RecyclerViewAdapter;
import com.xhb.uibase.demo.R;
import com.xhb.uibase.demo.core.annotation.Author;
import com.xhb.uibase.demo.core.Component;
import com.xhb.uibase.demo.core.FragmentComponent;
import com.xhb.uibase.demo.core.ViewModel;
import com.xhb.uibase.demo.core.ViewStyles;
import com.xhb.uibase.demo.databinding.ButtonsBinding;
import com.xhb.uibase.demo.view.recycler.PaddingDecoration;

import java.util.Map;

@AutoService(Component.class)
@Author("cmguo")
public class ButtonsComponent extends FragmentComponent<ButtonsBinding, ButtonsComponent.Model, ButtonsComponent.Style> {

    private static final String TAG = "ButtonsComponent";

    public static class Model extends ViewModel {
        private Map<String, Integer> styles;

        public Model(ButtonsComponent component) {
            styles = Styles.buttonStyles(component.getContext());
        }

        public Map<String, Integer> getStyles() {
            return styles;
        }
    }

    public static class Style extends ViewStyles {
        public ButtonItemLayout itemLayout = new ButtonItemLayout(this);
        public RecyclerView.ItemDecoration itemDecoration = new PaddingDecoration();
        @Bindable
        public int width = 400;
    }

    public static class ButtonItemLayout extends RecyclerViewAdapter.UnitTypeItemLayout<Map.Entry<String, Integer>> {
        private final Style style;

        public ButtonItemLayout(Style style) {
            super(R.layout.button_item);
            this.style = style;
        }

        @Override
        public void bindView(ViewDataBinding binding, Map.Entry<String, Integer> item, int position) {
            super.bindView(binding, item, position);
            binding.setVariable(BR.style, style);
        }
    }

    // this should be in view model, but fragment may simplify things
    public RecyclerViewAdapter.OnItemClickListener buttonClicked = new RecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, Object object) {
            Log.d(TAG, "buttonClicked" + object);
        }
    };

    @Override
    public int group() {
        return R.string.group_buttons;
    }

    @Override
    public int icon() {
        return android.R.drawable.btn_plus;
    }

    @Override
    public int title() {
        return R.string.component_buttons;
    }

    @Override
    public int description() {
        return R.string.component_buttons_desc;
    }
}
