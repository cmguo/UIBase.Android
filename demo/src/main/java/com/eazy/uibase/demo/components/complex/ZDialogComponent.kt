package com.eazy.uibase.demo.components.complex

import com.google.auto.service.AutoService
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.Component

@AutoService(Component::class)
class ZDialogComponent : Component {
    override fun id(): Int {
        return R.id.component_z_dialogs
    }

    override fun group(): Int {
        return R.string.group_complex
    }

    override fun icon(): Int {
        return android.R.drawable.btn_plus
    }

    override fun title(): Int {
        return R.string.component_z_dialogs
    }

    override fun description(): Int {
        return R.string.component_z_dialogs_desc
    }

}