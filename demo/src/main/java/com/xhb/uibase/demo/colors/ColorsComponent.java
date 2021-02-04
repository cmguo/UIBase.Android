package com.xhb.uibase.demo.colors;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.google.auto.service.AutoService;
import com.xhb.uibase.binding.RecyclerViewAdapter;
import com.xhb.uibase.demo.R;
import com.xhb.uibase.demo.core.Author;
import com.xhb.uibase.demo.core.Component;
import com.xhb.uibase.demo.core.FragmentComponent;
import com.xhb.uibase.demo.core.ViewModel;
import com.xhb.uibase.demo.core.ViewStyle;
import com.xhb.uibase.demo.databinding.ColorsBinding;
import com.xhb.uibase.demo.view.recycler.PaddingDecoration;

import java.util.Map;

@AutoService(Component.class)
@Author("cmguo")
public class ColorsComponent extends FragmentComponent<ColorsBinding, ColorsComponent.Model, ColorsComponent.Style> {

    private static final String TAG = "ColorsComponent";

    public static class Model extends ViewModel {
        private Map<String, Integer> colors;
        public Model(ColorsComponent component) {
            colors = Colors.stdColors(component.getContext());
        }

        public Model(ColorsComponent2 component) {
            colors = Colors.nonStdColors(component.getContext());
        }

        public Map<String, Integer> getColors() {
            return colors;
        }
    }

    public static class Style extends ViewStyle {
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

    @Override
    public int group() {
        return R.string.group_colors;
    }

    @Override
    public int icon() {
        return android.R.drawable.btn_star;
    }

    @Override
    public int title() {
        return R.string.component_colors;
    }

    @Override
    public int description() {
        return R.string.component_colors_desc;
    }
}
