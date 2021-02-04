package com.eazy.uibase.demo.buttons;

import android.util.Log;

import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.google.auto.service.AutoService;
import com.eazy.uibase.binding.RecyclerViewAdapter;
import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.Author;
import com.eazy.uibase.demo.core.Component;
import com.eazy.uibase.demo.core.FragmentComponent;
import com.eazy.uibase.demo.core.ViewModel;
import com.eazy.uibase.demo.core.ViewStyle;
import com.eazy.uibase.demo.databinding.ButtonsBinding;
import com.eazy.uibase.demo.view.recycler.PaddingDecoration;

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

    public static class Style extends ViewStyle {
        public ButtonItemLayout itemLayout = new ButtonItemLayout(this);
        public RecyclerView.ItemDecoration itemDecoration = new PaddingDecoration();
        public int width = 400;
    }

    public static class ButtonItemLayout extends RecyclerViewAdapter.BaseItemLayout {
        private final Style style;

        public ButtonItemLayout(Style style) {
            super(R.layout.button_item);
            this.style = style;
        }

        @Override
        public void bindView(ViewDataBinding binding, Object item, int position) {
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
