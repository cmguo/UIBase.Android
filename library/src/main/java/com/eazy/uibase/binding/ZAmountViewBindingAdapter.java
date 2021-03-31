package com.eazy.uibase.binding;

import android.view.View;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.eazy.uibase.widget.ZNumberView;

import org.jetbrains.annotations.NotNull;

public class ZAmountViewBindingAdapter {

    @InverseBindingAdapter(attribute = "amount", event = "amountAttrChanged")
    public static int getAmount(ZNumberView view) {
        return view.getAmount();
    }

    @BindingAdapter(value = {"onAmountChanged", "amountAttrChanged"},
            requireAll = false)
    public static void setListeners(ZNumberView view, final ZNumberView.OnAmountChangeListener listener,
                                    final InverseBindingListener attrChange) {
        if (attrChange == null) {
            view.setOnAmountChangeListener(listener);
        } else {
            view.setOnAmountChangeListener(new ZNumberView.OnAmountChangeListener() {
                @Override
                public void onAmountChanged(@NotNull View view, int amount) {
                    if (listener != null) {
                        listener.onAmountChanged(view, amount);
                    }
                    attrChange.onChange();
                }
            });
        }
    }

}
