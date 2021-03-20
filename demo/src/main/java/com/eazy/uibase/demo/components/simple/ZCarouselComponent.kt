package com.eazy.uibase.demo.components.simple

import com.google.auto.service.AutoService
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.Component

@AutoService(Component::class)
class ZCarouselComponent : Component {
    override fun id(): Int {
        return R.id.component_z_carousels
    }

    override fun group(): Int {
        return R.string.group_data_view
    }

    override fun icon(): Int {
        return android.R.drawable.btn_plus
    }

    override fun title(): Int {
        return R.string.component_z_carousels
    }

    override fun description(): Int {
        return R.string.component_z_carousels_desc
    }

}