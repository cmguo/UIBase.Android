package com.eazy.uibase.demo.components.optional

import com.google.auto.service.AutoService
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.Component
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.annotation.Author

@AutoService(Component::class)
@Author("cmguo")
class ZActivityViewComponent : Component {
    override fun id(): Int {
        return R.id.component_z_activity_views
    }

    override fun group(): Int {
        return R.string.group_menu_list
    }

    override fun icon(): Int {
        return android.R.drawable.btn_plus
    }

    override fun title(): Int {
        return R.string.component_z_activity_views
    }

    override fun description(): Int {
        return R.string.component_z_activity_views_desc
    }

    override fun fragmentClass(): Class<out ComponentFragment<*, *, *>> {
        return ZActivityViewFragment::class.java
    }

}