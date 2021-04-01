package com.eazy.uibase.demo.components.simple

import com.google.auto.service.AutoService
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.components.complex.ZDialogFragment
import com.eazy.uibase.demo.core.Component
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.annotation.Author

@AutoService(Component::class)
@Author("jiangzhiguo")
class ZListComponent : Component {
    override fun id(): Int {
        return R.id.component_z_lists
    }

    override fun group(): Int {
        return R.string.group_data_view
    }

    override fun icon(): Int {
        return android.R.drawable.btn_plus
    }

    override fun title(): Int {
        return R.string.component_z_lists
    }

    override fun description(): Int {
        return R.string.component_z_lists_desc
    }

    override fun fragmentClass(): Class<out ComponentFragment<*, *, *>?> {
        return ZListFragment::class.java
    }
}