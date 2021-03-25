package com.xhb.uibase.demo.components.complex

import com.google.auto.service.AutoService
import com.xhb.uibase.demo.R
import com.xhb.uibase.demo.core.Component
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.annotation.Author

@AutoService(Component::class)
@Author("jiangzhiguo")
class XHBDialogComponent : Component {
    override fun id(): Int {
        return R.id.component_xhb_dialogs
    }

    override fun group(): Int {
        return R.string.group_complex
    }

    override fun icon(): Int {
        return android.R.drawable.btn_plus
    }

    override fun title(): Int {
        return R.string.component_xhb_dialogs
    }

    override fun description(): Int {
        return R.string.component_xhb_dialogs_desc
    }

    override fun fragmentClass(): Class<out ComponentFragment<*, *, *>?> {
        return XHBDialogFragment::class.java
    }
}