package com.eazy.uibase.demo.resources;

import android.content.res.Configuration;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.eazy.uibase.demo.BR;
import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.ComponentFragment;
import com.eazy.uibase.demo.core.ViewModel;
import com.eazy.uibase.demo.core.ViewStyles;
import com.eazy.uibase.demo.core.style.annotation.Description;
import com.eazy.uibase.demo.core.style.annotation.Title;
import com.eazy.uibase.demo.databinding.DrawablesFragmentBinding;
import com.eazy.uibase.view.list.PaddingDecoration;
import com.eazy.uibase.view.list.RecyclerViewAdapter;
import com.eazy.uibase.view.list.UnitTypeItemBinding;

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
        public ItemBinding itemBinding = new ItemBinding(this);
        public RecyclerView.ItemDecoration itemDecoration = new PaddingDecoration();

        @Bindable
        @Title("图标变色")
        @Description("图标变色")
        public boolean drawableTint = false;
    }


    public static class ItemBinding extends UnitTypeItemBinding {

        private final Styles styles;

        public ItemBinding(Styles styles) {
            super(R.layout.drawable_item);
            this.styles = styles;
        }

        @Override
        public void bindView(ViewDataBinding binding, Object item, int position) {
            super.bindView(binding, item, position);
            binding.setVariable(BR.styles, styles);
        }
    }

    // this should be in view model, but fragment may simplify things
    public RecyclerViewAdapter.OnItemClickListener drawableClicked = (rv, v, position, object) -> Log.d(TAG, "drawableClicked" + object);

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getStyles().notifyPropertyChanged(BR.drawableTint);
    }
}
