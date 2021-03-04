package com.xhb.uibase.demo.components.basic

import com.google.auto.service.AutoService
import com.xhb.uibase.demo.R
import com.xhb.uibase.demo.core.Component
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.annotation.Author

@AutoService(Component::class)
@Author("Cmguo")
class XHBButtonComponent2 : Component {
    override fun id(): Int {
        return R.id.xhb_component_buttons2
    }

    override fun group(): Int {
        return R.string.group_basic
    }

    override fun icon(): Int {
        return android.R.drawable.btn_plus
    }

    override fun title(): Int {
        return R.string.component_xhb_buttons2
    }

    override fun description(): Int {
        return R.string.component_xhb_buttons_des
    }

    override fun fragmentClass(): Class<out ComponentFragment<*, *, *>?> {
        return XHBButtonFragment2::class.java
    }
}