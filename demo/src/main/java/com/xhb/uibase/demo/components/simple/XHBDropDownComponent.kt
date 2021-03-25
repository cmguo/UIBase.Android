package com.xhb.uibase.demo.components.simple

import com.google.auto.service.AutoService
import com.xhb.uibase.demo.R
import com.xhb.uibase.demo.core.Component
import com.xhb.uibase.demo.core.ComponentFragment

@AutoService(Component::class)
class XHBDropDownComponent : Component {
    override fun id(): Int {
        return R.id.component_xhb_drop_downs
    }

    override fun group(): Int {
        return R.string.group_simple
    }

    override fun icon(): Int {
        return android.R.drawable.btn_plus
    }

    override fun title(): Int {
        return R.string.component_xhb_drop_downs
    }

    override fun description(): Int {
        return R.string.component_xhb_drop_downs_desc
    }

    override fun fragmentClass(): Class<out ComponentFragment<*, *, *>> {
        return XHBDropDownFragment::class.java
    }
}