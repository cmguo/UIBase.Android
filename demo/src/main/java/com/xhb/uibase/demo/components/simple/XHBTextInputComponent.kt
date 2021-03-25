package com.xhb.uibase.demo.components.simple

import com.google.auto.service.AutoService
import com.xhb.uibase.demo.R
import com.xhb.uibase.demo.core.Component
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.annotation.Author

@AutoService(Component::class)
@Author("jianghziguo")
class XHBTextInputComponent : Component {
    override fun id(): Int {
        return R.id.component_xhb_text_inputs
    }

    override fun group(): Int {
        return R.string.group_simple
    }

    override fun icon(): Int {
        return android.R.drawable.btn_plus
    }

    override fun title(): Int {
        return R.string.component_xhb_text_inputs
    }

    override fun description(): Int {
        return R.string.component_xhb_text_inputs_desc
    }

    override fun fragmentClass(): Class<out ComponentFragment<*, *, *>?> {
        return XHBTextAreaFragment::class.java
    }
}