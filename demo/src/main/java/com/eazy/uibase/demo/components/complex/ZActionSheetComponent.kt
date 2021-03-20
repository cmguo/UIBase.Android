package com.eazy.uibase.demo.components.complex

import com.google.auto.service.AutoService
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.Component

@AutoService(Component::class)
class ZActionSheetComponent : Component {
    override fun id(): Int {
        return R.id.component_z_action_sheets
    }

    override fun group(): Int {
        return R.string.group_complex
    }

    override fun icon(): Int {
        return android.R.drawable.btn_plus
    }

    override fun title(): Int {
        return R.string.component_z_action_sheets
    }

    override fun description(): Int {
        return R.string.component_z_action_sheets_desc
    }

}