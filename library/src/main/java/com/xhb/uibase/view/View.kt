package com.xhb.uibase.view

import android.view.View
import android.view.ViewGroup
import androidx.core.view.get

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
