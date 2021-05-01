package com.eazy.uibase.demo.resources;

import android.content.res.Configuration;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.recyclerview.widget.RecyclerView;

import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.ComponentFragment;
import com.eazy.uibase.demo.core.ViewModel;
import com.eazy.uibase.demo.core.ViewStyles;
import com.eazy.uibase.demo.databinding.ColorsFragmentBinding;
import com.eazy.uibase.view.list.PaddingDecoration;
import com.eazy.uibase.view.list.RecyclerViewAdapter;

import java.util.Map;

public class ColorsFragment extends ComponentFragment<ColorsFragmentBinding, ColorsFragment.Model, ColorsFragment.Styles> {

    private static final String TAG = "ColorsComponent";

    public static class Model extends ViewModel {

        private Map<String, Resources.ResourceValue> colors;
        private Map<String, Colors.StateColor[]> stateColors;

        public Model(ColorsFragment fragment) {
            if (fragment.getComponent().id() == R.id.component_std_dynamic_colors)
                colors = Colors.stdDynamicColors(fragment.getContext());
            else if (fragment.getComponent().id() == R.id.component_std_static_colors)
                colors = Colors.stdStaticColors(fragment.getContext());
            else
                stateColors = Colors.stateListColors(fragment.getContext());
        }

        @Bindable
        public Map<String, ?> getColors() {
            return colors == null ? stateColors : colors;
        }

    }

    public static class Styles extends ViewStyles {
        public int itemBinding = R.layout.color_item;
        public RecyclerView.ItemDecoration itemDecoration = new PaddingDecoration();

        public Styles(ColorsFragment fragment) {
            if (fragment.getComponent().id() == R.id.component_state_list_colors) {
                itemBinding = R.layout.state_list_color_item;
            }
        }
    }

    // this should be in view model, but fragment may simplify things
    public RecyclerViewAdapter.OnItemClickListener<Map.Entry<String, Integer>> colorClicked = (position, object) -> Log.d(TAG, "colorClicked" + object);

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Colors.updateResources(requireContext().getResources(), getModel().colors);
    }
}
