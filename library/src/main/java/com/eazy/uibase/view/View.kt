package com.eazy.uibase.view

import android.view.View
import android.view.ViewGroup
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

fun <T: View> View.parentOfType(type: Class<T>) : T? {
    if (type.isInstance(this))
        return this as T
    val parent = parent
    if (parent is View)
        return parent.parentOfType(type)
    return null
}

fun <T: View> View.parentWithId(viewId : Int) : T? {
    if (this.id == viewId)
        return this as T
    val parent = parent
    if (parent is View)
        return parent.parentWithId(viewId)
    return null
}

fun <T> View.getTagInTree(tag: Int) : T? {
    val t = getTag(tag)
    if (t != null)
        return t as T
    val parent = parent
    if (parent is View)
        return parent.getTagInTree(tag)
    return null
}

val View.dataBinding get() : ViewDataBinding? = DataBindings.get(this)

val View.containingDataBinding get() : ViewDataBinding? = DataBindingUtil.findBinding(this)

val View.contentView: FrameLayout? get() {
    return rootView.findViewById<View>(android.R.id.content) as? FrameLayout
}
