package com.xhb.uibase.demo.components.simple

import com.google.auto.service.AutoService
import com.xhb.uibase.demo.R
import com.xhb.uibase.demo.core.Component

@AutoService(Component::class)
class XHBCarouselComponent : Component {
    override fun id(): Int {
        return R.id.component_xhb_carousels
    }

    override fun group(): Int {
        return R.string.group_data_view
    }

    override fun icon(): Int {
        return android.R.drawable.btn_plus
    }

    override fun title(): Int {
        return R.string.component_xhb_carousels
    }

    override fun description(): Int {
        return R.string.component_xhb_carousels_desc
    }

}