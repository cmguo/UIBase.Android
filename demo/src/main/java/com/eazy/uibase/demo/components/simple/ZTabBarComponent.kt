package com.eazy.uibase.demo.components.simple

import com.google.auto.service.AutoService
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.Component

@AutoService(Component::class)
class ZTabBarComponent : Component {
    override fun id(): Int {
        return R.id.component_z_tab_bars
    }

    override fun group(): Int {
        return R.string.group_navigation
    }

    override fun icon(): Int {
        return android.R.drawable.btn_plus
    }

    override fun title(): Int {
        return R.string.component_z_tab_bars
    }

    override fun description(): Int {
        return R.string.component_z_tab_bars_desc
    }

}