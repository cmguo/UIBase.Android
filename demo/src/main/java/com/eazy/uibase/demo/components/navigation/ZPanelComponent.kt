package com.eazy.uibase.demo.components.navigation

import com.google.auto.service.AutoService
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.Component
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.annotation.Author

@AutoService(Component::class)
@Author("cmguo")
class ZPanelComponent : Component {
    override fun id(): Int {
        return R.id.component_z_panels
    }

    override fun group(): Int {
        return R.string.group_navigation
    }

    override fun icon(): Int {
        return 0
    }

    override fun title(): Int {
        return R.string.component_z_panels
    }

    override fun description(): Int {
        return R.string.component_z_panels
    }

    override fun fragmentClass(): Class<out ComponentFragment<*, *, *>> {
        return ZPanelFragment::class.java
    }
}