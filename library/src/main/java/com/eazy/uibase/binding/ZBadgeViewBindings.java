package com.eazy.uibase.binding;

import android.util.SizeF;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.eazy.uibase.widget.badgeview.ZBadgeView;

public class ZBadgeViewBindings {

    @BindingAdapter(value = "borderColor")
    public static void setBadgeBorderWidth(final ZBadgeView view, final int color) {
        view.setColorBackgroundBorder(color);
    }

    @BindingAdapter(value = "borderWidth")
    public static void setBadgeBorderWidth(final ZBadgeView view, final float width) {
        view.setBackgroundBorderWidth(width);
    }

    @BindingAdapter(value = "badgeGravity")
    public static void setBadgeGravity(final ZBadgeView view, final int gravity) {
        view.setBadgeGravity(gravity);
    }

    @BindingAdapter(value = "gravityOffset")
    public static void setBadgeGravity(final ZBadgeView view, final SizeF offset) {
        view.setGravityOffset(offset.getWidth(), offset.getHeight(), false);
    }

    @BindingAdapter(value = "draggable")
    public static void setDraggable(final ZBadgeView view, final boolean draggable) {
        view.setDraggable(draggable);
    }

    @BindingAdapter(value = "exactly")
    public static void setExactly(final ZBadgeView view, final boolean exactly) {
        view.setExactMode(exactly);
    }

    @BindingAdapter(value = "badgeNumber")
    public static void setBadgeNumber(final ZBadgeView view, final int number) {
        view.setBadgeNumber(number);
    }

    @BindingAdapter(value = "badgeText")
    public static void setBadgeNumber(final ZBadgeView view, final CharSequence text) {
        view.setBadgeText(text.toString());
    }

    @BindingAdapter(value = "dragState")
    public static void setBadgeNumber(final ZBadgeView view, final ZBadgeView.DragState state) {
    }

    @BindingAdapter(value = {"dragStateChanged", "dragStateAttrChanged"}, requireAll = false)
    public static void setDragStateChangedListener(final ZBadgeView view,
                                                   final ZBadgeView.OnDragStateChangedListener listener,
                                                   final InverseBindingListener dragStateAttrChanged) {
        if (listener != null) {
            view.setOnDragStateChangedListener(listener);
        } else if (dragStateAttrChanged != null) {
            view.setOnDragStateChangedListener((ZBadgeView.DragState dragState, ZBadgeView badge, View targetView) -> {
                dragStateAttrChanged.onChange();
            });
        } else {
            view.setOnDragStateChangedListener(null);
        }
    }

    @InverseBindingAdapter(attribute = "dragState", event = "dragStateAttrChanged")
    public static ZBadgeView.DragState dragStateChanged(ZBadgeView view) {
        return view.getDragState();
    }

}
