package com.xhb.uibase.demo.components.simple

import com.google.auto.service.AutoService
import com.xhb.uibase.demo.R
import com.xhb.uibase.demo.core.Component
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.annotation.Author

@AutoService(Component::class)
@Author("cmguo")
class XHBAppTitleBarComponent : Component {
    override fun id(): Int {
        return R.id.component_xhb_app_title_bars
    }

    override fun group(): Int {
        return R.string.group_navigation
    }

    override fun icon(): Int {
        return android.R.drawable.btn_plus
    }

    override fun title(): Int {
        return R.string.component_xhb_app_title_bars
    }

    override fun description(): Int {
        return R.string.component_xhb_app_title_bars_desc
    }

    override fun fragmentClass(): Class<out ComponentFragment<*, *, *>> {
        return XHBAppTitleBarFragment::class.java
    }
}