package com.eazy.uibase.binding;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.eazy.uibase.widget.ZCheckBox;

public class ZCheckBoxBindingAdapter {

    @InverseBindingAdapter(attribute = "checkedState", event = "checkedStateAttrChanged")
    public static ZCheckBox.CheckedState getCheckedState(ZCheckBox view) {
        return view.getCheckedState();
    }

    @BindingAdapter(value = {"onCheckedStateChanged", "checkedStateAttrChanged"},
            requireAll = false)
    public static void setListeners(ZCheckBox view, final ZCheckBox.OnCheckedStateChangeListener listener,
                                    final InverseBindingListener attrChange) {
        if (attrChange == null) {
            view.setOnCheckedStateChangeListener(listener);
        } else {
            view.setOnCheckedStateChangeListener((checkBox, state) -> {
                if (listener != null) {
                    listener.onCheckedStateChanged(checkBox, state);
                }
                attrChange.onChange();
            });
        }
    }

}
