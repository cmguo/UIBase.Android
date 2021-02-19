package com.eazy.uibase.demo.layouts;

import android.app.Dialog;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.eazy.uibase.binding.RecyclerViewAdapter;
import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.ComponentFragment;
import com.eazy.uibase.demo.core.Layouts;
import com.eazy.uibase.demo.core.ViewModel;
import com.eazy.uibase.demo.core.ViewStyles;
import com.eazy.uibase.demo.databinding.DialogsBinding;
import com.eazy.uibase.demo.view.recycler.PaddingDecoration;

import java.util.Map;

public class DialogsFragment extends ComponentFragment<DialogsBinding, DialogsFragment.Model, DialogsFragment.Style> {

    private static final String TAG = "DialogsFragment";

    public static class Model extends ViewModel {
        private Map<String, Integer> styles;

        public Model(DialogsFragment fragment) {
            styles = Layouts.dialogLayouts(fragment.getContext());
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
            Dialog dialog = new Dialog(DialogsFragment.this.getContext());
            try {
                dialog.setContentView(((Map.Entry<String, Integer>) object).getValue());
                dialog.show();
            } catch (Throwable e) {
                Log.w(TAG, "", e);
            }
        }
    };
}
