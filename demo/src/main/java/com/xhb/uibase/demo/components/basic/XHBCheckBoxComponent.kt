package com.xhb.uibase.demo.components.basic

import com.google.auto.service.AutoService
import com.xhb.uibase.demo.R
import com.xhb.uibase.demo.core.Component
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.annotation.Author

@AutoService(Component::class)
@Author("cmguo")
class XHBCheckBoxComponent : Component {
    override fun id(): Int {
        return R.id.component_xhb_check_boxes
    }

    override fun group(): Int {
        return R.string.group_basic
    }

    override fun icon(): Int {
        return R.drawable.img_share_moment
    }

    override fun title(): Int {
        return R.string.component_xhb_check_boxes
    }

    override fun description(): Int {
        return R.string.component_xhb_check_boxes_desc
    }

    override fun fragmentClass(): Class<out ComponentFragment<*, *, *>?> {
        return XHBCompoundButtonFragment::class.java
    }
}