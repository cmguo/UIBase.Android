package com.xhb.uibase.binding;

import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.BindingAdapter;

import com.xhb.uibase.view.list.SpinnerAdapter;

import java.util.List;

public class SpinnerBindingAdapter {

    @BindingAdapter("entries")
    public static <T> void setSpinnerEntries(AppCompatSpinner spinner, List<T> data) {
        SpinnerAdapter adapter = getAdapter(spinner);
        if (adapter != null) {
            adapter.clear();
            adapter.addAll(data);
        }
    }

    @BindingAdapter("selection")
    public static <T> void setSpinnerSelection(AppCompatSpinner spinner, int selection) {
        SpinnerAdapter adapter = getAdapter(spinner);
        if (adapter != null) {
            spinner.setSelection(selection);
        }
    }

    @BindingAdapter("itemLayout")
    public static void setSpinnerItemLayout(AppCompatSpinner spinner, int layout) {
        SpinnerAdapter adapter = getAdapter(spinner);
        if (adapter != null) {
            adapter.setDropDownViewResource(layout);
        }
    }

    @BindingAdapter("itemSelected")
    public static <T> void setSpinnerOnItemSelectedListener(AppCompatSpinner spinner, OnItemSelectedListener<T> listener) {
        SpinnerAdapter adapter = getAdapter(spinner);
        if (adapter != null) {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    listener.onItemSelected(position, (T) adapter.getItem(position));
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    listener.onItemSelected(-1, null);
                }
            });
        }
    }

    @FunctionalInterface
    public interface OnItemSelectedListener<T> {
        void onItemSelected(int position, T object);
    }

    private static SpinnerAdapter getAdapter(AppCompatSpinner spinner) {
        android.widget.SpinnerAdapter adapter = spinner.getAdapter();
        if (adapter == null) {
            adapter = new SpinnerAdapter(spinner.getContext());
            spinner.setAdapter(adapter);
        }
        return adapter instanceof SpinnerAdapter ? (SpinnerAdapter) adapter : null;
    }

}
