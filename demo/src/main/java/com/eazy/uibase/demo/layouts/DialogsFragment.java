package com.eazy.uibase.demo.layouts;

import android.app.Dialog;
import android.graphics.drawable.StateListDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.ComponentFragment;
import com.eazy.uibase.demo.core.Layouts;
import com.eazy.uibase.demo.core.ViewModel;
import com.eazy.uibase.demo.core.ViewStyles;
import com.eazy.uibase.demo.databinding.DialogsFragmentBinding;
import com.eazy.uibase.view.list.PaddingDecoration;
import com.eazy.uibase.view.list.RecyclerViewAdapter;

import java.util.Map;

public class DialogsFragment extends ComponentFragment<DialogsFragmentBinding, DialogsFragment.Model, DialogsFragment.Styles>
        implements View.OnClickListener {

    private static final String TAG = "DialogsFragment";

    public static class Model extends ViewModel {
        private Map<String, Integer> layouts;

        public Model(DialogsFragment fragment) {
            layouts = Layouts.dialogLayouts(fragment.getContext());
        }

        public Map<String, Integer> getLayouts() {
            return layouts;
        }
    }

    public static class Styles extends ViewStyles {
        public int itemBinding = R.layout.dialog_item;
        public RecyclerView.ItemDecoration itemDecoration = new PaddingDecoration();
    }

    // this should be in view model, but fragment may simplify things
    public RecyclerViewAdapter.OnItemClickListener dialogClicked = new RecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, Object object) {
            Log.d(TAG, "dialogClicked" + object);
            Dialog dialog = new Dialog(DialogsFragment.this.getContext(), 0);
            try {
                int layoutId = ((Map.Entry<String, Integer>) object).getValue();
                View view = LayoutInflater.from(getActivity()).inflate(layoutId, null);
                applyStyles(view);
                dialog.setContentView(view);
                dialog.show();
            } catch (Throwable e) {
                Log.w(TAG, "", e);
            }
        }
    };

    private void applyStyles(View view) {
        view.setVisibility(View.VISIBLE);
        if (view instanceof ViewGroup) {
            for (int i = 0, n = ((ViewGroup) view).getChildCount(); i < n; ++i) {
                applyStyles(((ViewGroup) view).getChildAt(i));
            }
        } else if (view instanceof TextView) {
            if (((TextView) view).getText().length() == 0)
                ((TextView) view).setText("文字");
        }
        if (view.getBackground() instanceof StateListDrawable) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

    }

}
