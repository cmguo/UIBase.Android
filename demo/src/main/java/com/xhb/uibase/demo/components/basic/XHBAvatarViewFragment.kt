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
        @Title("技术方案")
        @Description("剪切技术方案，有 View，Drawable 两种方式")
        var roundMode = XHBAvatarView.RoundMode.Drawable

        @Bindable
        @Title("边框宽度")
        @Description("加上边框；")
        var borderWidth = 0f

        @Bindable
        @Title("边框颜色")
        @Description("边框颜色")
        @Style(ColorStyle::class)
        var borderColor = Color.RED

        @Bindable
        @Title("填充颜色")
        @Description("填充颜色")
        @Style(ColorStyle::class)
        var fillColor = Color.BLUE

    }

    companion object {
        private const val TAG = "XHBAvatarViewFragment"
    }
}