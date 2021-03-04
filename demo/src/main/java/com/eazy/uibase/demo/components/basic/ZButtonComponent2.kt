package com.eazy.uibase.demo.components.basic

import com.google.auto.service.AutoService
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.Component
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.annotation.Author

@AutoService(Component::class)
@Author("Cmguo")
class ZButtonComponent2 : Component {
    override fun id(): Int {
        return R.id.component_buttons2
    }

    override fun group(): Int {
        return R.string.group_basic
    }

    override fun icon(): Int {
        return android.R.drawable.btn_plus
    }

    override fun title(): Int {
        return R.string.component_z_buttons2
    }

    override fun description(): Int {
        return R.string.component_z_buttons_des
    }

    override fun fragmentClass(): Class<out ComponentFragment<*, *, *>?> {
        return ZButtonFragment2::class.java
    }
}