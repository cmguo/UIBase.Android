package com.eazy.uibase.demo.resources;

import android.content.res.Configuration;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.ComponentFragment;
import com.eazy.uibase.demo.core.ViewModel;
import com.eazy.uibase.demo.core.ViewStyles;
import com.eazy.uibase.demo.databinding.DrawablesFragmentBinding;
import com.eazy.uibase.view.list.PaddingDecoration;
import com.eazy.uibase.view.list.RecyclerViewAdapter;

import java.util.Map;

public class DrawablesFragment extends ComponentFragment<DrawablesFragmentBinding, DrawablesFragment.Model, DrawablesFragment.Styles> {

    private static final String TAG = "DrawablesComponent";

    public static class Model extends ViewModel {

        public Map<String, Resources.ResourceValue> drawables;

        public Model(DrawablesFragment fragment) {
            if (fragment.getComponent().id() == R.id.component_icons)
                drawables = Drawables.icons(fragment.getContext());
            else if (fragment.getComponent().id() == R.id.component_images)
                drawables = Drawables.images(fragment.getContext());
            else
                drawables = Drawables.dynamicImages(fragment.getContext());
        }

    }

    public static class Styles extends ViewStyles {
        public int itemBinding = R.layout.drawable_item;
        public RecyclerView.ItemDecoration itemDecoration = new PaddingDecoration();
    }

    // this should be in view model, but fragment may simplify things
    public RecyclerViewAdapter.OnItemClickListener<Map.Entry<String, Integer>> drawableClicked = (position, object) -> Log.d(TAG, "drawableClicked" + object);

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Drawables.updateResources(requireContext().getResources(), getModel().drawables);
    }
}
