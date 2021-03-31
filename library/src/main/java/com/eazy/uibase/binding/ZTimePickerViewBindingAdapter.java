package com.eazy.uibase.binding;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.eazy.uibase.widget.ZTimePickerView;

import java.util.Date;

public class ZTimePickerViewBindingAdapter {

    @InverseBindingAdapter(attribute = "selectTime", event = "selectTimeAttrChanged")
    public static Date getSelectTime(ZTimePickerView view) {
        return view.getSelectTime();
    }


    @BindingAdapter(value = {"onSelectTimeChanged", "selectTimeAttrChanged"},
            requireAll = false)
    public static void setListeners(ZTimePickerView view, final ZTimePickerView.OnSelectTimeChangeListener listener,
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
