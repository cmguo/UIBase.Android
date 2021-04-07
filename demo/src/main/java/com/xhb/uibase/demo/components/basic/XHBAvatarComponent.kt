package com.xhb.uibase.demo.components.basic

import com.google.auto.service.AutoService
import com.xhb.uibase.demo.R
import com.xhb.uibase.demo.core.Component
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.annotation.Author

@AutoService(Component::class)
@Author("cmguo")
class XHBAvatarComponent : Component {
    override fun id(): Int {
        return R.id.component_xhb_avatars
    }

    override fun group(): Int {
        return R.string.group_basic
    }

    override fun icon(): Int {
        return R.drawable.img_share_weixin
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