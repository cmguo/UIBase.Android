package com.eazy.uibase.dialog

import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.eazy.uibase.R
import java.lang.ref.WeakReference

class MaskDialog(private val content: View?) : DialogFragment() {

    private val layoutParams = content?.layoutParams

    constructor() : this(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_Dialog_Z_Mask)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return content
    }

    override fun onStart() {
        content?.setTag(R.id.dialog_container, WeakReference(this))
        if (layoutParams != null) {
            val window = dialog?.window
            val params = window?.attributes
            params?.width = layoutParams.width
            params?.height = layoutParams.height
            if (layoutParams is FrameLayout.LayoutParams)
                params?.gravity = layoutParams.gravity
            window?.attributes = params
        }
        super.onStart()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (content == null) return
        super.show(manager, tag)
    }

    companion object {
        fun dismiss(content: View) {
            val dialog = content.getTag(R.id.dialog_container) as? WeakReference<*>
            (dialog?.get() as? MaskDialog)?.dismiss()
        }
    }

}