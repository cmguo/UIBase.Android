package com.eazy.uibase.demo.components.input

import android.os.Bundle
import android.view.View
import androidx.databinding.Bindable
import com.ui.shapeutils.DevShapeUtils
import com.ui.shapeutils.shape.DevShape
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.DimenStyle
import com.eazy.uibase.demo.core.style.IconStyle
import com.eazy.uibase.demo.core.style.annotation.*
import com.eazy.uibase.demo.databinding.TextAreaFragmentBinding
import kotlinx.android.synthetic.main.text_area_fragment.*

class ZTextAreaFragment : ComponentFragment<TextAreaFragmentBinding?, ZTextAreaFragment.Model?, ZTextAreaFragment.Styles?>() {

    class Model : ViewModel()

    class Styles : ViewStyles() {

        @Bindable
        @Title("最大字数") @Description("设为0不限制；如果有限制，将展现字数指示")
        var maximumCharCount = 100

        @Bindable
        @Title("最小高度") @Description("没有文字时的高度")
        @Style(DimenStyle::class)
        var minimunHeight = 100f

        @Bindable
        @Title("最大高度") @Description("高度随文字变化，需要指定最大高度；包含字数指示（如果有的话）")
        @Style(DimenStyle::class)
        var maximunHeight = 300f

        @Bindable
        @Title("占位文字") @Description("没有任何输入文字时，显示的占位文字（灰色）")
        var placeholder = "请输入"

        @Bindable
        @Title("显示边框") @Description("设为0不限制；如果有限制，将展现字数指示")
        var showBorder = false

        @Bindable
        @Title("左图标") @Description("设置左边的图标，资源ID类型，控件内部会自动重新布局")
        @Style(IconStyle::class)
        var leftIcon = 0

        @Bindable
        @Title("右图标") @Description("设置右边的图标，资源ID类型，控件内部会自动重新布局")
        @Style(IconStyle::class)
        var rightIcon = 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        labelEditText0.background =
            DevShapeUtils.shape(DevShape.RECTANGLE).solid(R.color.white_card).radius(50f).build()
        float_edit_text_error.background =
            DevShapeUtils.shape(DevShape.RECTANGLE).solid(R.color.white_card).radius(50f).build()

        sms_edit_text.setOnSmsCodeClickedListener {
//            Toast.makeText(this,"已发送手机验证码至手机",Toast.LENGTH_SHORT)
        }
        error_style_2.editText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && error_style_2.editText.text.isNotEmpty()) {
                error_style_2.setErrorMessage("出错了")
            } else {
                error_style_2.setErrorMessage("")
            }
        }
        float_edit_text_error.setErrorMessage("出错了")
    }

    companion object {
        private const val TAG = "ZTextAreaFragment"
    }
}