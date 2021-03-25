package com.eazy.uibase.demo.components.simple

import com.google.auto.service.AutoService
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.Component
import com.eazy.uibase.demo.core.ComponentFragment

@AutoService(Component::class)
class ZTabComponent : Component {
    override fun id(): Int {
        return R.id.component_z_tabs
    }

    override fun group(): Int {
        return R.string.group_navigation
    }

    override fun icon(): Int {
        return android.R.drawable.btn_plus
    }

    override fun title(): Int {
        return R.string.component_z_tabs
    }

    override fun description(): Int {
        return R.string.component_z_tabs_desc
    }

    override fun fragmentClass(): Class<out ComponentFragment<*, *, *>> {
        return ZTabFragment::class.java
    }
}