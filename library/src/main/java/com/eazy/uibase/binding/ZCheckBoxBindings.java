package com.eazy.uibase.binding;

import android.util.SizeF;
import android.view.View;
import android.widget.CompoundButton;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.eazy.uibase.widget.ZCheckBox;
import com.eazy.uibase.widget.badgeview.ZBadgeView;

public class ZCheckBoxBindings {

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
            view.setOnCheckedStateChangeListener(new ZCheckBox.OnCheckedStateChangeListener() {
                @Override
                public void onCheckedStateChanged(ZCheckBox checkBox, ZCheckBox.CheckedState state) {
                    if (listener != null) {
                        listener.onCheckedStateChanged(checkBox, state);
                    }
                    attrChange.onChange();
                }
            });
        }
    }

}
