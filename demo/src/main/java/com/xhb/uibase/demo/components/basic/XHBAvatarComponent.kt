package com.xhb.uibase.demo.components.basic

import com.google.auto.service.AutoService
import com.xhb.uibase.demo.R
import com.xhb.uibase.demo.core.Component
import com.xhb.uibase.demo.core.ComponentFragment

@AutoService(Component::class)
class XHBAvatarComponent : Component {
    override fun id(): Int {
        return R.id.component_xhb_avatars
    }

    override fun group(): Int {
        return R.string.group_basic
    }

    override fun icon(): Int {
        return android.R.drawable.btn_plus
    }

    override fun title(): Int {
        return R.string.component_xhb_avatars
    }

    override fun description(): Int {
        return R.string.component_xhb_avatars_desc
    }

    override fun fragmentClass(): Class<out ComponentFragment<*, *, *>?> {
        return XHBAvatarViewFragment::class.java
    }
}