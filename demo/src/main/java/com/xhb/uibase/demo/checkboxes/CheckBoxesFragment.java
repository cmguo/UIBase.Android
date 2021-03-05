package com.xhb.uibase.demo.checkboxes;

import android.os.Bundle;
import android.util.Log;

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
import com.xhb.uibase.demo.databinding.CheckBoxesFragmentBinding;
import com.xhb.uibase.demo.view.recycler.PaddingDecoration;

import java.util.Map;

import skin.support.observe.SkinObservable;
import skin.support.observe.SkinObserver;

public class CheckBoxesFragment extends ComponentFragment<CheckBoxesFragmentBinding, CheckBoxesFragment.Model, CheckBoxesFragment.Style>
        implements SkinObserver {

    private static final String TAG = "ButtonsComponent";

    public static class Model extends ViewModel {
        private Map<String, Integer> styles;

        public Model(CheckBoxesFragment fragment) {
            if (fragment.getComponent().id() == R.id.component_check_boxes)
                styles = Styles.checkboxStyles(fragment.getContext());
            else
                styles = Styles.radioStyles(fragment.getContext());
        }

        public Map<String, Integer> getStyles() {
            return styles;
        }
    }

    public static class Style extends ViewStyles {
        public ItemLayout itemLayout = new ItemLayout(this);
        public RecyclerView.ItemDecoration itemDecoration = new PaddingDecoration();

        @Bindable @Title("文字")
        public String text = "选择我吗？";
        @Bindable @Title("禁用")
        public boolean disabled = false;
    }

    private void updateWidth() {
        RecyclerView listView = getView().findViewById(R.id.buttonsList);
        listView.measure(0,listView.getHeight());
        // TODO: 让按钮变小
    }

    public static class ItemLayout extends RecyclerViewAdapter.UnitTypeItemLayout<Map.Entry<String, Integer>> {
        private final Style style;

        public ItemLayout(Style style) {
            super(R.layout.check_box_item);
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
            Log.d(TAG, "checkBoxClicked" + object);
        }
    };

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
