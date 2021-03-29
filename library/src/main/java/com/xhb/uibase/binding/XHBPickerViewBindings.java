package com.xhb.uibase.binding;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.xhb.uibase.widget.XHBPickerView;

import java.util.List;

public class XHBPickerViewBindings {

    @InverseBindingAdapter(attribute = "selections", event = "selectionAttrChanged")
    public static List<Integer> getSelections(XHBPickerView view) {
        return view.getSelections();
    }

    @InverseBindingAdapter(attribute = "selection", event = "selectionAttrChanged")
    public static Integer getSelection(XHBPickerView view) {
        return view.getSelection();
    }

    @BindingAdapter(value = {"onSelectionChanged", "selectionAttrChanged"},
            requireAll = false)
    public static void setListeners(XHBPickerView view, final XHBPickerView.OnSelectionChangeListener listener,
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
