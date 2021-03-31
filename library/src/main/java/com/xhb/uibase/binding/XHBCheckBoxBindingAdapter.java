package com.xhb.uibase.binding;

import android.util.SizeF;
import android.view.View;
import android.widget.CompoundButton;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.xhb.uibase.widget.XHBCheckBox;
import com.xhb.uibase.widget.badgeview.XHBBadgeView;

public class XHBCheckBoxBindingAdapter {

    @InverseBindingAdapter(attribute = "checkedState", event = "checkedStateAttrChanged")
    public static XHBCheckBox.CheckedState getCheckedState(XHBCheckBox view) {
        return view.getCheckedState();
    }

    @BindingAdapter(value = {"onCheckedStateChanged", "checkedStateAttrChanged"},
            requireAll = false)
    public static void setListeners(XHBCheckBox view, final XHBCheckBox.OnCheckedStateChangeListener listener,
                                    final InverseBindingListener attrChange) {
        if (attrChange == null) {
            view.setOnCheckedStateChangeListener(listener);
        } else {
            view.setOnCheckedStateChangeListener(new XHBCheckBox.OnCheckedStateChangeListener() {
                @Override
                public void onCheckedStateChanged(XHBCheckBox checkBox, XHBCheckBox.CheckedState state) {
                    if (listener != null) {
                        listener.onCheckedStateChanged(checkBox, state);
                    }
                    attrChange.onChange();
                }
            });
        }
    }

}
