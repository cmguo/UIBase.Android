package com.eazy.uibase.demo.components.simple

import com.google.auto.service.AutoService
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.Component
import com.eazy.uibase.demo.core.ComponentFragment

@AutoService(Component::class)
class ZNoticeBarComponent : Component {
    override fun id(): Int {
        return R.id.component_z_notice_bars
    }

    override fun group(): Int {
        return R.string.group_simple
    }

    override fun icon(): Int {
        return android.R.drawable.btn_plus
    }

    override fun title(): Int {
        return R.string.component_z_notice_bars
    }

    override fun description(): Int {
        return R.string.component_z_notice_bars_desc
    }

    override fun fragmentClass(): Class<out ComponentFragment<*, *, *>?> {
        return ZTipViewFragment::class.java
    }
}