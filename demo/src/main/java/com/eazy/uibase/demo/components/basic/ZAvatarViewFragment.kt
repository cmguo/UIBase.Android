package com.eazy.uibase.demo.components.basic

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.Bindable
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.ColorStyle
import com.eazy.uibase.demo.core.style.DimenStyle
import com.eazy.uibase.demo.core.style.annotation.Description
import com.eazy.uibase.demo.core.style.annotation.Style
import com.eazy.uibase.demo.core.style.annotation.Title
import com.eazy.uibase.demo.databinding.AvatarViewFragmentBinding
import com.eazy.uibase.resources.Drawables
import com.eazy.uibase.resources.ViewDrawable
import com.eazy.uibase.widget.ZAvatarView

class ZAvatarViewFragment : ComponentFragment<AvatarViewFragmentBinding?,
    ZAvatarViewFragment.Model?, ZAvatarViewFragment.Styles?>() {

    class Model(fragment: ZAvatarViewFragment) : ViewModel() {
        val drawable = ViewDrawable(fragment.requireContext(), R.layout.avatar_text)
    }

    class Styles(private val fragment: ZAvatarViewFragment) : ViewStyles() {

        @Bindable
        @Title("剪切方式")
        @Description("剪切图片的方式，有 None，Circle，Ellipse，RoundSquare，RoundRect五种方式，默认Circle方式")
        var clipType = ZAvatarView.ClipType.Circle

        @Bindable
        @Title("剪切区域")
        @Description("剪切区域，有视图区域（WholeView）、图片区域（Drawable）两种方式")
        var clipRegion = ZAvatarView.ClipRegion.Drawable

        @Bindable
        @Title("剪切半径")
        @Description("圆角剪切时，剪切圆角半径，设为0，则为直角")
        @Style(DimenStyle::class)
        var cornerRadius = 0f

        @Bindable
        @Title("边框宽度")
        @Description("设置头像边框宽度，设为0，则没有边框")
        @Style(DimenStyle::class)
        var borderWidth = 0.5f

        @Bindable
        @Title("边框颜色")
        @Description("设置头像边框颜色")
        @Style(ColorStyle::class)
        var borderColor = Color.RED

        @Bindable
        @Title("头像文字")
        @Description("设置文字头像的文字，结合使用 ViewDrawable 和 @layout/avatar_text")
        var text = "头像"
            set(value) {
                field = value
                val view = fragment.model.drawable.getView() as? TextView
                view?.text = value
                ((view?.parent) as? ViewGroup)?.requestLayout()
            }
    }

    companion object {
        private const val TAG = "ZAvatarViewFragment"
    }

}