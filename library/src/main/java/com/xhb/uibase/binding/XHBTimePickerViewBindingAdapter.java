package com.xhb.uibase.binding;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.xhb.uibase.widget.XHBTimePickerView;

import java.util.Date;

public class XHBTimePickerViewBindingAdapter {

    @InverseBindingAdapter(attribute = "selectTime", event = "selectTimeAttrChanged")
    public static Date getSelectTime(XHBTimePickerView view) {
        return view.getSelectTime();
    }


    @BindingAdapter(value = {"onSelectTimeChanged", "selectTimeAttrChanged"},
            requireAll = false)
    public static void setListeners(XHBTimePickerView view, final XHBTimePickerView.OnSelectTimeChangeListener listener,
                                    final InverseBindingListener attrChange) {
        if (attrChange == null) {
            view.setListener(listener);
        } else {
            view.setListener((picker, time) -> {
                if (listener != null) {
                    listener.onSelectTimeChanged(picker, time);
                }
                attrChange.onChange();
            });
        }
    }

}
