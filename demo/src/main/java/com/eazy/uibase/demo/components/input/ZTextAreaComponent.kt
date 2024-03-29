package com.eazy.uibase.demo.components.input

import com.google.auto.service.AutoService
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.Component
import com.eazy.uibase.demo.core.ComponentFragment

@AutoService(Component::class)
class ZTextAreaComponent : Component {
    override fun id(): Int {
        return R.id.component_z_text_areas
    }

    override fun group(): Int {
        return R.string.group_input
    }

    override fun icon(): Int {
        return 0
    }

    override fun title(): Int {
        return R.string.component_z_text_areas
    }

    override fun description(): Int {
        return R.string.component_z_text_areas_desc
    }

    override fun fragmentClass(): Class<out ComponentFragment<*, *, *>?> {
        return ZTextAreaFragment::class.java
    }
}