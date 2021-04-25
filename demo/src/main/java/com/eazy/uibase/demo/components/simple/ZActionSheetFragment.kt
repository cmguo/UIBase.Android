package com.eazy.uibase.demo.components.simple

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.Bindable
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.IconStyle
import com.eazy.uibase.demo.core.style.annotation.Description
import com.eazy.uibase.demo.core.style.annotation.Style
import com.eazy.uibase.demo.core.style.annotation.Title
import com.eazy.uibase.demo.databinding.ActionSheetFragmentBinding
import com.eazy.uibase.widget.ZActionSheet
import com.eazy.uibase.widget.ZPanel
import com.eazy.uibase.widget.ZTipView

class ZActionSheetFragment : ComponentFragment<ActionSheetFragmentBinding?, ZActionSheetFragment.Model?, ZActionSheetFragment.Styles?>()
    , ZPanel.PanelListener, ZActionSheet.ActionSheetListener {

    class Model : ViewModel() {

        // selected first
        var states = arrayListOf<Any?>(android.R.attr.state_selected)

        @Bindable
        val buttons: List<String> = listOf("删除内容", "列表标题")
    }

    class Styles : ViewStyles() {

        @Bindable
        @Title("提示图标")
        @Description("附加在文字左侧的图标，资源ID类型，不能点击")
        @Style(IconStyle::class)
        var icon = 0

        @Bindable
        @Title("标题")
        @Description("标题文字，一般在中间显示")
        var title = "标题"

        @Bindable
        @Title("详细描述")
        @Description("描述文字，显示在标题下面")
        var subTitle = "真的要撤回该作业吗？\n" +
            "所有已提交的学生作业也会被删除！"

    }

    companion object {
        private const val TAG = "ZActionSheetFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.actionSheet.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bluegrey_00))
        return view
    }

    override fun backgroundColor(): Int {
        return R.color.blue_100
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.actionSheet.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bluegrey_00))
    }

    private var lp : ViewGroup.LayoutParams? = null

    var buttonClick = View.OnClickListener {
        val panel = ZPanel(requireContext())
        panel.bottomButton = R.string.cancel
        panel.listener = this
        binding.frame.removeView(binding.actionSheet)
        lp = binding.actionSheet.layoutParams
        binding.actionSheet.setBackgroundColor(0)
        panel.addView(binding.actionSheet)
        panel.popUp(parentFragmentManager)
    }

    override fun panelDismissed(panel: ZPanel) {
        panel.removeView(binding.actionSheet)
        binding.actionSheet.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bluegrey_00))
        binding.frame.addView(binding.actionSheet, lp)
    }

    override fun onAction(sheet: ZActionSheet, index: Int) {
       val tip = ZTipView(requireContext())
        tip.message="点击了按钮${index}"
        tip.location = ZTipView.Location.AutoToast
        tip.popAt(sheet)
    }

}