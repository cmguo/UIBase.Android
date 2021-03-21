package com.xhb.uibase.binding;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.BindingAdapter;

import java.util.List;

public class SpinnerAdapter<T> extends ArrayAdapter implements android.widget.SpinnerAdapter {

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

    public SpinnerAdapter(@NonNull Context context) {
        super(context, 0);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }
}
