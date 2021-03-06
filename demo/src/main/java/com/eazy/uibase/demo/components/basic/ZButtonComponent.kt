package com.eazy.uibase.demo.components.basic

import com.google.auto.service.AutoService
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.Component
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.annotation.Author

@AutoService(Component::class)
@Author("cmguo")
class ZButtonComponent : Component {
    override fun id(): Int {
        return R.id.component_z_buttons
    }

    override fun group(): Int {
        return R.string.group_basic
    }

    override fun icon(): Int {
        return android.R.drawable.btn_plus
    }

    override fun title(): Int {
        return R.string.component_z_buttons
    }

    override fun description(): Int {
        return R.string.component_z_buttons_des
    }

    override fun fragmentClass(): Class<out ComponentFragment<*, *, *>?> {
        return ZButtonFragment::class.java
    }
}