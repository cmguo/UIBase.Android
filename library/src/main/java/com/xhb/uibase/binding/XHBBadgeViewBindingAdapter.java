package com.xhb.uibase.binding;

import android.util.SizeF;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.xhb.uibase.widget.badgeview.XHBBadgeView;

public class XHBBadgeViewBindingAdapter {

    @BindingAdapter(value = "borderColor")
    public static void setBadgeBorderWidth(final XHBBadgeView view, final int color) {
        view.setColorBackgroundBorder(color);
    }

    @BindingAdapter(value = "borderWidth")
    public static void setBadgeBorderWidth(final XHBBadgeView view, final float width) {
        view.setBackgroundBorderWidth(width);
    }

    @BindingAdapter(value = "badgeGravity")
    public static void setBadgeGravity(final XHBBadgeView view, final int gravity) {
        view.setBadgeGravity(gravity);
    }

    @BindingAdapter(value = "gravityOffset")
    public static void setBadgeGravity(final XHBBadgeView view, final SizeF offset) {
        view.setGravityOffset(offset.getWidth(), offset.getHeight(), false);
    }

    @BindingAdapter(value = "draggable")
    public static void setDraggable(final XHBBadgeView view, final boolean draggable) {
        view.setDraggable(draggable);
    }

    @BindingAdapter(value = "exactly")
    public static void setExactly(final XHBBadgeView view, final boolean exactly) {
        view.setExactMode(exactly);
    }

    @BindingAdapter(value = "badgeNumber")
    public static void setBadgeNumber(final XHBBadgeView view, final int number) {
        view.setBadgeNumber(number);
    }

    @BindingAdapter(value = "badgeText")
    public static void setBadgeNumber(final XHBBadgeView view, final CharSequence text) {
        view.setBadgeText(text.toString());
    }

    @BindingAdapter(value = "dragState")
    public static void setBadgeNumber(final XHBBadgeView view, final XHBBadgeView.DragState state) {
    }

    @BindingAdapter(value = {"dragStateChanged", "dragStateAttrChanged"}, requireAll = false)
    public static void setDragStateChangedListener(final XHBBadgeView view,
                                                   final XHBBadgeView.OnDragStateChangedListener listener,
                                                   final InverseBindingListener dragStateAttrChanged) {
        if (listener != null) {
            view.setOnDragStateChangedListener(listener);
        } else if (dragStateAttrChanged != null) {
            view.setOnDragStateChangedListener((XHBBadgeView.DragState dragState, XHBBadgeView badge, View targetView) -> {
                dragStateAttrChanged.onChange();
            });
        } else {
            view.setOnDragStateChangedListener(null);
        }
    }

    @InverseBindingAdapter(attribute = "dragState", event = "dragStateAttrChanged")
    public static XHBBadgeView.DragState dragStateChanged(XHBBadgeView view) {
        return view.getDragState();
    }

}
