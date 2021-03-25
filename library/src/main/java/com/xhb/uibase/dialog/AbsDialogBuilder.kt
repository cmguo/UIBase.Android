package com.xhb.uibase.dialog

import android.content.Context
import android.content.DialogInterface

abstract class AbsDialogBuilder<T>(val context: Context) : IDialogBuilder<T> {

    protected var title: String? = null
    protected var titleStyle: ContentStyle? = null
    protected var onShowListener: DialogInterface.OnShowListener? = null
    protected var onDismissListener: DialogInterface.OnDismissListener? = null
    protected var cancelable = true

    abstract fun getBuilderInstance(): T

    override fun withTitle(titleResId: Int): T = this.withTitle(context.resources.getString(titleResId))

    override fun withTitle(title: String): T {
        this.title = title
        return getBuilderInstance()
    }

    override fun withTitle(titleResId: Int, contentStyle: ContentStyle?): T = this.withTitle(context.resources.getString(titleResId), contentStyle)

    override fun withTitle(title: String, contentStyle: ContentStyle?): T {
        this.title = title
        this.titleStyle = titleStyle
        return getBuilderInstance()
    }

    override fun setCancelable(cancelable: Boolean): T {
        this.cancelable = cancelable
        return getBuilderInstance()
    }

    override fun setOnShowListener(onShowListener: DialogInterface.OnShowListener): T {
        this.onShowListener = onShowListener
        return getBuilderInstance()
    }

    override fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener): T {
        this.onDismissListener = onDismissListener
        return getBuilderInstance()
    }
}
