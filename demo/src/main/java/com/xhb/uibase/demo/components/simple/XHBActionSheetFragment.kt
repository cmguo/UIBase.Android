package com.xhb.uibase.demo.components.simple

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Bindable
import com.xhb.uibase.demo.BR
import com.xhb.uibase.demo.R
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.ViewModel
import com.xhb.uibase.demo.core.ViewStyles
import com.xhb.uibase.demo.core.style.IconStyle
import com.xhb.uibase.demo.core.style.annotation.Description
import com.xhb.uibase.demo.core.style.annotation.Style
import com.xhb.uibase.demo.core.style.annotation.Title
import com.xhb.uibase.demo.databinding.XhbActionSheetFragmentBinding
import com.xhb.uibase.widget.XHBActionSheet
import com.xhb.uibase.widget.XHBPanel
import com.xhb.uibase.widget.XHBTipView

class XHBActionSheetFragment : ComponentFragment<XhbActionSheetFragmentBinding?, XHBActionSheetFragment.Model?, XHBActionSheetFragment.Styles?>()
    , XHBPanel.PanelListener, XHBActionSheet.ActionSheetListener {

    class Model : ViewModel() {

        @Bindable
        val titles: List<String> = listOf("删除内容", "列表标题")
    }

    class Styles : ViewStyles() {

        @Bindable
        @Title("提示图标")
        @Description("附加在文字左侧的图标，资源ID类型，不能点击")
        @Style(IconStyle::class)
        var icon = 0

        @Bindable
        @Title("标题")
        @Description("标题文字，一般在中间显示；如果没有左侧按钮内容，则在左侧大标题样式展示")
        var title = "标题"

        @Bindable
        @Title("详细描述")
        @Description("描述文字，一般在中间显示；如果没有左侧按钮内容，则在左侧大标题样式展示")
        var subTitle = "真的要撤回该作业吗？\n" +
            "所有已提交的学生作业也会被删除！"

    }

    companion object {
        private const val TAG = "XHBActionSheetFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.actionSheet.setBackgroundColor(Color.WHITE)
        return view
    }

    override fun backgroundColor(): Int {
        return R.color.blue_100
    }

    private var lp : ViewGroup.LayoutParams? = null

    var buttonClick = View.OnClickListener {
        val panel = XHBPanel(requireContext())
        panel.bottomButton = R.string.cancel
        panel.listener = this
        binding.frame.removeView(binding.actionSheet)
        lp = binding.actionSheet.layoutParams
        binding.actionSheet.setBackgroundColor(0)
        panel.addView(binding.actionSheet)
        panel.popUp(parentFragmentManager)
    }

    override fun panelDismissed(panel: XHBPanel) {
        panel.removeView(binding.actionSheet)
        binding.actionSheet.setBackgroundColor(Color.WHITE)
        binding.frame.addView(binding.actionSheet, lp)
    }

    override fun onAction(sheet: XHBActionSheet, index: Int) {
       val tip = XHBTipView(requireContext())
        tip.message="点击了按钮${index}"
        tip.location = XHBTipView.Location.AutoToast
        tip.popAt(requireView())
    }

}