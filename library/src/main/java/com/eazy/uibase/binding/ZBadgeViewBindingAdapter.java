package com.eazy.uibase.binding;

import android.util.SizeF;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.eazy.uibase.widget.ZBadgeView;

public class ZBadgeViewBindingAdapter {

    @InverseBindingAdapter(attribute = "dragState", event = "dragStateAttrChanged")
    public static ZBadgeView.DragState getDragState(final ZBadgeView view) {
        return view.getDragState();
    }

    @BindingAdapter("dragState")
    public static void setDragState(final ZBadgeView view, ZBadgeView.DragState state) {
    }

    @BindingAdapter(value = {"dragStateChanged", "dragStateAttrChanged"}, requireAll = false)
    public static void setDragStateChangedListener(final ZBadgeView view,
                                                   final ZBadgeView.OnDragStateChangeListener listener,
                                                   final InverseBindingListener dragStateAttrChanged) {
        if (listener != null) {
            view.setOnDragStateChangeListener(listener);
        } else if (dragStateAttrChanged != null) {
            view.setOnDragStateChangeListener((ZBadgeView badge, ZBadgeView.DragState dragState) -> {
                dragStateAttrChanged.onChange();
            });
        } else {
            view.setOnDragStateChangedListener(null);
        }
    }

}
