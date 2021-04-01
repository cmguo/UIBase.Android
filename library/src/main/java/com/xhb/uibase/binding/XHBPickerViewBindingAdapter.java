package com.xhb.uibase.binding;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.xhb.uibase.widget.XHBPickerView;

import java.util.List;

public class XHBPickerViewBindingAdapter {

    @InverseBindingAdapter(attribute = "selections", event = "selectionsAttrChanged")
    public static List<Integer> getSelections(XHBPickerView view) {
        return view.getSelections();
    }

    @InverseBindingAdapter(attribute = "selection", event = "selectionAttrChanged")
    public static Integer getSelection(XHBPickerView view) {
        return view.getSelection();
    }

    @BindingAdapter(value = {"onSelectionChanged", "selectionsAttrChanged", "selectionAttrChanged"},
            requireAll = false)
    public static void setListeners(XHBPickerView view, final XHBPickerView.OnSelectionChangeListener listener,
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
