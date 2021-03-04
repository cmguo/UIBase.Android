package com.eazy.uibase.demo.components.basic;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.eazy.uibase.binding.RecyclerViewAdapter;
import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.ComponentFragment;
import com.eazy.uibase.demo.core.SkinManager;
import com.eazy.uibase.demo.core.Styles;
import com.eazy.uibase.demo.core.ViewModel;
import com.eazy.uibase.demo.core.ViewStyles;
import com.eazy.uibase.demo.core.annotation.Description;
import com.eazy.uibase.demo.core.annotation.Title;
import com.eazy.uibase.demo.core.annotation.Values;
import com.eazy.uibase.demo.databinding.XhbButtonFragmentBinding;
import com.eazy.uibase.demo.view.recycler.PaddingDecoration;
import com.eazy.uibase.widget.ZButton;
import com.eazy.uibase.widget.ZButtonLoadingView;

import java.util.Map;

import skin.support.observe.SkinObservable;
import skin.support.observe.SkinObserver;

public class ZButtonFragment extends ComponentFragment<XhbButtonFragmentBinding, ZButtonFragment.Model, ZButtonFragment.Style> implements SkinObserver {

    private static final String TAG = "ZButtonFragment";

    public static class Model extends ViewModel {
        private Map<String, Integer> styles;

        public Model(ZButtonFragment fragment) {
            styles = Styles.xhbButtonStyles(fragment.getContext());
        }

        public Map<String, Integer> getStyles() {
            return styles;
        }
    }

    public static class Style extends ViewStyles {

        enum ButtonWidth {
            WrapContent(ViewGroup.LayoutParams.WRAP_CONTENT),
            MatchParent(ViewGroup.LayoutParams.MATCH_PARENT),
            ;

            private int layoutWidth_;

            ButtonWidth(int layoutWidth) {
                layoutWidth_ = layoutWidth;
            }

            public int layoutWidth() {
                return layoutWidth_;
            }
        }

        public ButtonItemLayout itemLayout = new ButtonItemLayout(this);
        public RecyclerView.ItemDecoration itemDecoration = new PaddingDecoration();

        @Bindable
        @Title("禁用") @Description("切换到禁用状态")
        public boolean disabled = false;

        @Bindable
        @Title("加载") @Description("切换到加载状态")
        public boolean loading = false;

        @Bindable
        @Title("尺寸模式") @Description("有下列尺寸模式：大（Large）、中（Middle）、小（Small），默认：Large")
        public ZButton.ButtonSize sizeMode = ZButton.ButtonSize.Large;

        @Bindable
        @Title("宽度模式") @Description("有下列宽度模式：适应内容（WrapContent）、适应布局（MatchParent），默认：WrapContent")
        public ButtonWidth widthMode = ButtonWidth.WrapContent;

        @Bindable
        @Title("文字") @Description("改变文字，按钮会自动适应文字宽度")
        public String text = "按钮";

        private ZButtonFragment fragment_;

        public Style(ZButtonFragment fragment) {
            this.fragment_ = fragment;
        }

    }

    private void updateWidth() {
        RecyclerView listView = getView().findViewById(R.id.buttonsList);
        listView.measure(0, listView.getHeight());
        // TODO: 让按钮变小
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
    public RecyclerViewAdapter.OnItemClickListener buttonClicked = (int position, Object object) -> {
        Log.d(TAG, "buttonClicked " + object);
    };

    public void onClick(View view) {

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


    public static void testButtonClick(View view) {
        if (view.getParent() instanceof ZButtonLoadingView) {
            ZButtonLoadingView loadingView = (ZButtonLoadingView) view.getParent();
            if(!loadingView.isLoading()) {
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        testButtonClick(view);
                    }
                }, 3000);
            }
            loadingView.setLoading(!loadingView.isLoading());
        }
    }
}
