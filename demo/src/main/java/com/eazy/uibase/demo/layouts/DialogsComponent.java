package com.eazy.uibase.demo.layouts;

import android.app.Dialog;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.google.auto.service.AutoService;
import com.eazy.uibase.binding.RecyclerViewAdapter;
import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.annotation.Author;
import com.eazy.uibase.demo.core.Component;
import com.eazy.uibase.demo.core.FragmentComponent;
import com.eazy.uibase.demo.core.ViewModel;
import com.eazy.uibase.demo.core.ViewStyles;
import com.eazy.uibase.demo.databinding.DialogsBinding;
import com.eazy.uibase.demo.view.recycler.PaddingDecoration;

import java.util.Map;

@AutoService(Component.class)
@Author("cmguo")
public class DialogsComponent extends FragmentComponent<DialogsBinding, DialogsComponent.Model, DialogsComponent.Style> {

    private static final String TAG = "DialogsComponent";

    public static class Model extends ViewModel {
        private Map<String, Integer> styles;

        public Model(DialogsComponent component) {
            styles = Layouts.dialogLayouts(component.getContext());
        }

        public Map<String, Integer> getStyles() {
            return styles;
        }
    }

    public static class Style extends ViewStyles {
        public int itemLayout = R.layout.dialog_item;
        public RecyclerView.ItemDecoration itemDecoration = new PaddingDecoration();
    }

    // this should be in view model, but fragment may simplify things
    public RecyclerViewAdapter.OnItemClickListener dialogClicked = new RecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, Object object) {
            Log.d(TAG, "dialogClicked" + object);
            Dialog dialog = new Dialog(DialogsComponent.this.getContext());
            try {
                dialog.setContentView(((Map.Entry<String, Integer>) object).getValue());
                dialog.show();
            } catch (Throwable e) {
                Log.w(TAG, "", e);
            }
        }
    };

    @Override
    public int group() {
        return R.string.group_dialogs;
    }

    @Override
    public int icon() {
        return android.R.drawable.btn_plus;
    }

    @Override
    public int title() {
        return R.string.component_dialogs;
    }

    @Override
    public int description() {
        return R.string.component_dialogs_desc;
    }
}
