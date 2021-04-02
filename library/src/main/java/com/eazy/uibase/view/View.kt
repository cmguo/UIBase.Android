package com.eazy.uibase.view

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.eazy.uibase.binding.DataBindings

fun <T: View> View.findViewByType(type: Class<T>) : T? {
    if (type.isInstance(this))
        return this as T
    val group = this as? ViewGroup ?: return null
    for (i in 0 until group.childCount) {
        val c = group.get(i).findViewByType(type)
        if (c != null)
            return c
    }
    return null
}

val View.dataBinding get() = DataBindings.get(this)

val View.contentView: FrameLayout? get() {
    return rootView.findViewById<View>(android.R.id.content) as? FrameLayout
}
