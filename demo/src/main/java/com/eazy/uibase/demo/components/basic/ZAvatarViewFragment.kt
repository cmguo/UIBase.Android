package com.eazy.uibase.demo.components.basic

import android.graphics.Color
import androidx.databinding.Bindable
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.ColorStyle
import com.eazy.uibase.demo.core.style.annotation.Description
import com.eazy.uibase.demo.core.style.annotation.Style
import com.eazy.uibase.demo.core.style.annotation.Title
import com.eazy.uibase.demo.databinding.AvatarViewFragmentBinding
import com.eazy.uibase.widget.ZAvatarView

class ZAvatarViewFragment : ComponentFragment<AvatarViewFragmentBinding?,
    ZAvatarViewFragment.Model?, ZAvatarViewFragment.Styles?>() {

    class Model : ViewModel() {
    }

    class Styles(private val fragment_: ZAvatarViewFragment) : ViewStyles() {

        @Bindable
        @Title("剪切方式")
        @Description("剪切图片的方式，有 None，Circle，Ellipse三种方式，默认Circle方式")
        var clipType = ZAvatarView.ClipType.Circle

        @Bindable
        @Title("剪切区域")
        @Description("剪切区域，有视图区域（WholeView）、图片区域（Drawable）两种方式")
        var clipRegion = ZAvatarView.ClipRegion.Drawable

        @Bindable
        @Title("边框宽度")
        @Description("设置头像边框宽度，设为0，则没有边框")
        var borderWidth = 0f

        @Bindable
        @Title("边框颜色")
        @Description("设置头像边框颜色")
        @Style(ColorStyle::class)
        var borderColor = Color.RED
    }

    companion object {
        private const val TAG = "ZAvatarViewFragment"
    }
}