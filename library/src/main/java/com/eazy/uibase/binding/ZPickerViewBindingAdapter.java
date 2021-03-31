package com.eazy.uibase.binding;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.eazy.uibase.widget.ZPickerView;

import java.util.List;

public class ZPickerViewBindingAdapter {

    @InverseBindingAdapter(attribute = "selections", event = "selectionsAttrChanged")
    public static List<Integer> getSelections(ZPickerView view) {
        return view.getSelections();
    }

    @InverseBindingAdapter(attribute = "selection", event = "selectionAttrChanged")
    public static Integer getSelection(ZPickerView view) {
        return view.getSelection();
    }

    @BindingAdapter(value = {"onSelectionChanged", "selectionsAttrChanged", "selectionAttrChanged"},
            requireAll = false)
    public static void setListeners(ZPickerView view, final ZPickerView.OnSelectionChangeListener listener,
                                    final InverseBindingListener attrChange,
                                    final InverseBindingListener attrChange2) {
        if (attrChange == null && attrChange2 == null) {
            view.setListener(listener);
        } else {
            view.setListener(picker -> {
                if (listener != null) {
                    listener.onSelectionChanged(picker);
                }
                if (attrChange != null)
                    attrChange.onChange();
                if (attrChange2 != null)
                    attrChange2.onChange();
            });
        }
    }

}
