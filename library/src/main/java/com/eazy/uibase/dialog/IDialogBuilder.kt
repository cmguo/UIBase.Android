package com.eazy.uibase.dialog

import android.content.DialogInterface
import androidx.annotation.StringRes

interface IDialogBuilder<T> {
    fun withTitle(title: String): T

    fun withTitle(title: String, contentStyle: ContentStyle?): T

    fun withTitle(@StringRes titleResId: Int): T

    fun withTitle(@StringRes titleResId: Int, contentStyle: ContentStyle?): T

    fun setCancelable(cancelable: Boolean): T

    fun setOnShowListener(onShowListener: DialogInterface.OnShowListener): T

    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener): T
}
