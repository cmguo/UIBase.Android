package com.eazy.uibase.dialog

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.eazy.uibase.R

/**
 * BaseDialogFragment
 */
open class BaseDialogFragment : DialogFragment() {

    var dismissListener: OnDismissListener? = null
    var cancelListener: OnCancelListener? = null

    open fun show(manager: FragmentManager) {
        show(manager, javaClass.name)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!isAdded && !isVisible && !isRemoving) {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.MyDialogFragment)
    }

    override fun dismiss() {
        dismissAllowingStateLoss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissListener?.onDismiss(dialog, this)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        cancelListener?.onCancel(dialog, this)
    }

    public interface OnDismissListener {
        fun onDismiss(dialog: DialogInterface, fragment: DialogFragment)
    }

    public interface OnCancelListener {
        fun onCancel(dialog: DialogInterface, fragment: DialogFragment)
    }
}
