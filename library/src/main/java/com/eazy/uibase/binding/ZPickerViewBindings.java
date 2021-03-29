package com.eazy.uibase.binding;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.eazy.uibase.widget.ZPickerView;

import java.util.List;

public class ZPickerViewBindings {

    @InverseBindingAdapter(attribute = "selections", event = "selectionAttrChanged")
    public static List<Integer> getSelections(ZPickerView view) {
        return view.getSelections();
    }

    @InverseBindingAdapter(attribute = "selection", event = "selectionAttrChanged")
    public static Integer getSelection(ZPickerView view) {
        return view.getSelection();
    }

    @BindingAdapter(value = {"onSelectionChanged", "selectionAttrChanged"},
            requireAll = false)
    public static void setListeners(ZPickerView view, final ZPickerView.OnSelectionChangeListener listener,
                                    final InverseBindingListener attrChange) {
        if (attrChange == null) {
            view.setListener(listener);
        } else {
            view.setListener(picker -> {
                if (listener != null) {
                    listener.onSelectionChanged(picker);
                }
                attrChange.onChange();
            });
        }
    }

}
