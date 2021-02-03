package com.eazy.uibase.demo.colors;

import android.content.Context;
import android.util.Log;

import com.google.auto.service.AutoService;
import com.eazy.uibase.binding.RecyclerViewAdapter;
import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.Author;
import com.eazy.uibase.demo.core.Component;
import com.eazy.uibase.demo.core.FragmentComponent;
import com.eazy.uibase.demo.core.ViewModel;
import com.eazy.uibase.demo.core.ViewStyle;
import com.eazy.uibase.demo.databinding.ColorsBinding;

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
    }

    // this should be in view model, but fragment may simplify things
    public RecyclerViewAdapter.OnItemClickListener colorClicked = new RecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, Object object) {
            Log.d(TAG, "colorClicked" + object);
        }
    };

    public ColorsComponent() {
        super(R.layout.test);
    }

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
