package com.eazy.uibase.demo.colors;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.recyclerview.widget.RecyclerView;

import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.Colors;
import com.eazy.uibase.demo.core.ComponentFragment;
import com.eazy.uibase.demo.core.ViewModel;
import com.eazy.uibase.demo.core.ViewStyles;
import com.eazy.uibase.demo.databinding.ColorsFragmentBinding;
import com.eazy.uibase.view.list.PaddingDecoration;
import com.eazy.uibase.view.list.RecyclerViewAdapter;

import java.util.Map;
import java.util.Objects;

public class ColorsFragment extends ComponentFragment<ColorsFragmentBinding, ColorsFragment.Model, ColorsFragment.Styles> {

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
    public RecyclerViewAdapter.OnItemClickListener<Map.Entry<String, Integer>> colorClicked = (position, object) -> Log.d(TAG, "colorClicked" + object);

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getModel().updateColors(getContext());
        Objects.requireNonNull(getBinding().colorsList.getAdapter()).notifyItemRangeChanged(0, getModel().getColors().size());
    }
}
