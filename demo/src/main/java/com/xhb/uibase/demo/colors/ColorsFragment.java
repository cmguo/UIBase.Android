package com.xhb.uibase.demo.colors;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.xhb.uibase.binding.RecyclerViewAdapter;
import com.xhb.uibase.demo.R;
import com.xhb.uibase.demo.core.ComponentFragment;
import com.xhb.uibase.demo.core.ViewModel;
import com.xhb.uibase.demo.core.ViewStyles;
import com.xhb.uibase.demo.databinding.ColorsBinding;
import com.xhb.uibase.demo.view.recycler.PaddingDecoration;

import java.util.Map;

public class ColorsFragment extends ComponentFragment<ColorsBinding, ColorsFragment.Model, ColorsFragment.Style> {

    private static final String TAG = "ColorsComponent";

    public static class Model extends ViewModel {
        private Map<String, Integer> colors;
        public Model(ColorsFragment fragment) {
            if (fragment.getComponent().id() == R.id.component_colors)
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
