package com.eazy.uibase.demo.colors;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.eazy.uibase.binding.RecyclerViewAdapter;
import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.ComponentFragment;
import com.eazy.uibase.demo.core.ViewModel;
import com.eazy.uibase.demo.core.ViewStyles;
import com.eazy.uibase.demo.databinding.ColorsBinding;
import com.eazy.uibase.demo.view.recycler.PaddingDecoration;

import java.util.Map;

public class ColorsFragment extends ComponentFragment<ColorsBinding, ColorsFragment.Model, ColorsFragment.Style> {

    private static final String TAG = "ColorsComponent";

    public static class Model extends ViewModel {
        private Map<String, Integer> colors;
        public Model(ColorsFragment fragment) {
            if (fragment.getId() == R.id.component_colors)
                colors = Colors.stdColors(fragment.getContext());
            else
                colors = Colors.nonStdColors(fragment.getContext());
        }

        public Map<String, Integer> getColors() {
            return colors;
        }
    }

    public static class Style extends ViewStyles {
        public int itemLayout = R.layout.color_item;
        public RecyclerView.ItemDecoration itemDecoration = new PaddingDecoration();
    }

    // this should be in view model, but fragment may simplify things
    public RecyclerViewAdapter.OnItemClickListener colorClicked = new RecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, Object object) {
            Log.d(TAG, "colorClicked" + object);
        }
    };

}
