package com.eazy.uibase.demo.components.indicator

import com.google.auto.service.AutoService
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.Component
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.annotation.Author

@AutoService(Component::class)
@Author("cmguo")
class ZDialogComponent : Component {

    override fun id(): Int {
        return R.id.component_z_dialogs
    }

    override fun group(): Int {
        return R.string.group_indicator
    }

    override fun icon(): Int {
        return 0
    }

    override fun title(): Int {
        return R.string.component_z_dialogs
    }

    override fun description(): Int {
        return R.string.component_z_dialogs_desc
    }

    override fun fragmentClass(): Class<out ComponentFragment<*, *, *>?> {
        return ZDialogFragment::class.java
    }
}