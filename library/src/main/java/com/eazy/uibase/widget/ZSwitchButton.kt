package com.eazy.uibase.widget

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import androidx.appcompat.widget.SwitchCompat
import com.eazy.uibase.R
import com.eazy.uibase.resources.RoundDrawable

class ZSwitchButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : SwitchCompat(context, attrs, R.attr.switchButtonStyle) {

    init {
        trackDrawable = RoundDrawable(context, R.style.RoundDrawable_ZSwitchButton_Track)
        thumbDrawable = RoundDrawable(context, R.style.RoundDrawable_ZSwitchButton_Thumb)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        trackDrawable = RoundDrawable(context, R.style.RoundDrawable_ZSwitchButton_Track)
        thumbDrawable = RoundDrawable(context, R.style.RoundDrawable_ZSwitchButton_Thumb)
    }

    companion object {
        private const val TAG = "ZSwitchButton"
    }

}
