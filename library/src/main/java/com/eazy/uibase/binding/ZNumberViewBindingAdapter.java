package com.eazy.uibase.binding;

import android.view.View;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.eazy.uibase.widget.ZNumberView;

import org.jetbrains.annotations.NotNull;

public class ZNumberViewBindingAdapter {

    @InverseBindingAdapter(attribute = "number", event = "numberAttrChanged")
    public static int getNumber(ZNumberView view) {
        return view.getNumber();
    }

    @BindingAdapter(value = {"onNumberChanged", "numberAttrChanged"},
            requireAll = false)
    public static void setListeners(ZNumberView view, final ZNumberView.OnNumberChangeListener listener,
                                    final InverseBindingListener attrChange) {
        if (attrChange == null) {
            view.setOnNumberChangeListener(listener);
        } else {
            view.setOnNumberChangeListener(new ZNumberView.OnNumberChangeListener() {
                @Override
                public void onNumberChanged(@NotNull View view, int number) {
                    if (listener != null) {
                        listener.onNumberChanged(view, number);
                    }
                    attrChange.onChange();
                }
            });
        }
    }

}
