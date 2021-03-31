package com.xhb.uibase.binding;

import android.view.View;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.xhb.uibase.widget.XHBNumberView;

import org.jetbrains.annotations.NotNull;

public class XHBAmountViewBindingAdapter {

    @InverseBindingAdapter(attribute = "amount", event = "amountAttrChanged")
    public static int getAmount(XHBNumberView view) {
        return view.getAmount();
    }

    @BindingAdapter(value = {"onAmountChanged", "amountAttrChanged"},
            requireAll = false)
    public static void setListeners(XHBNumberView view, final XHBNumberView.OnAmountChangeListener listener,
                                    final InverseBindingListener attrChange) {
        if (attrChange == null) {
            view.setOnAmountChangeListener(listener);
        } else {
            view.setOnAmountChangeListener(new XHBNumberView.OnAmountChangeListener() {
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
