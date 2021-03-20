package com.xhb.uibase.demo.components.basic

import com.google.auto.service.AutoService
import com.xhb.uibase.demo.R
import com.xhb.uibase.demo.core.Component

@AutoService(Component::class)
class XHBAvatarComponent : Component {
    override fun id(): Int {
        return R.id.component_xhb_avatars
    }

    override fun group(): Int {
        return R.string.group_basic2
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

}