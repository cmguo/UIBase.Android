package com.xhb.uibase.widget

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import androidx.appcompat.widget.SwitchCompat
import com.xhb.uibase.R
import com.xhb.uibase.resources.RoundDrawable

class XHBSwitchButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SwitchCompat(context, attrs) {

    init {
        trackDrawable = RoundDrawable(context, R.style.XHBSwitchButton_Track)
        thumbDrawable = RoundDrawable(context, R.style.XHBSwitchButton_Thumb)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        trackDrawable = RoundDrawable(context, R.style.XHBSwitchButton_Track)
        thumbDrawable = RoundDrawable(context, R.style.XHBSwitchButton_Thumb)
    }

    companion object {
        private const val TAG = "XHBSwitchButton"
    }

}
