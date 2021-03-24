package com.xhb.uibase.demo.colors;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.recyclerview.widget.RecyclerView;

import com.xhb.uibase.binding.RecyclerViewAdapter;
import com.xhb.uibase.demo.R;
import com.xhb.uibase.demo.core.Colors;
import com.xhb.uibase.demo.core.ComponentFragment;
import com.xhb.uibase.demo.core.SkinManager;
import com.xhb.uibase.demo.core.ViewModel;
import com.xhb.uibase.demo.core.ViewStyles;
import com.xhb.uibase.demo.databinding.ColorsFragmentBinding;
import com.xhb.uibase.demo.view.recycler.PaddingDecoration;

import java.util.Map;

import skin.support.observe.SkinObservable;
import skin.support.observe.SkinObserver;

public class ColorsFragment extends ComponentFragment<ColorsFragmentBinding, ColorsFragment.Model, ColorsFragment.Styles>
    implements SkinObserver {

    private static final String TAG = "ColorsComponent";

    public static class Model extends ViewModel {

        private final Map<String, Integer> colors;

        public Model(ColorsFragment fragment) {
            if (fragment.getComponent().id() == R.id.component_colors)
                colors = Colors.stdColors(fragment.getContext());
            else
                colors = Colors.nonStdColors(fragment.getContext());
        }

        @Bindable
        public Map<String, Integer> getColors() {
            return colors;
        }

        void updateColors(Context context) {
            Colors.update(context, colors);
        }
    }

    public static class Styles extends ViewStyles {
        public int itemBinding = R.layout.color_item;
        public RecyclerView.ItemDecoration itemDecoration = new PaddingDecoration();
    }

    // this should be in view model, but fragment may simplify things
    public RecyclerViewAdapter.OnItemClickListener colorClicked = (position, object) -> Log.d(TAG, "colorClicked" + object);

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
        getModel().updateColors(getContext());
        getBinding().colorsList.getAdapter().notifyItemRangeChanged(0, getModel().getColors().size());
    }
}
