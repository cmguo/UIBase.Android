package com.xhb.uibase.demo.components.basic

import android.graphics.Color
import androidx.databinding.Bindable
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.ViewModel
import com.xhb.uibase.demo.core.ViewStyles
import com.xhb.uibase.demo.core.style.ColorStyle
import com.xhb.uibase.demo.core.style.annotation.Description
import com.xhb.uibase.demo.core.style.annotation.Style
import com.xhb.uibase.demo.core.style.annotation.Title
import com.xhb.uibase.demo.databinding.XhbAvatarViewFragmentBinding
import com.xhb.uibase.widget.XHBAvatarView

class XHBAvatarViewFragment : ComponentFragment<XhbAvatarViewFragmentBinding?,
    XHBAvatarViewFragment.Model?, XHBAvatarViewFragment.Styles?>() {

    class Model : ViewModel() {
    }

    class Styles(private val fragment_: XHBAvatarViewFragment) : ViewStyles() {

        @Bindable
        @Title("剪切方式")
        @Description("剪切图片的方式，有 None，Circle，Ellipse三种方式，默认Circle方式")
        var clipType = XHBAvatarView.ClipType.Circle

        @Bindable
        @Title("剪切区域")
        @Description("剪切区域，有视图区域（WholeView）、图片区域（Drawable）两种方式")
        var clipRegion = XHBAvatarView.ClipRegion.Drawable

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
        private const val TAG = "XHBAvatarViewFragment"
    }
}