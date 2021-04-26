package com.eazy.uibase.demo.components.indicator

import android.view.View
import android.view.ViewGroup
import androidx.databinding.Bindable
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.ColorStyle
import com.eazy.uibase.demo.core.style.ContentStyle
import com.eazy.uibase.demo.core.style.annotation.Description
import com.eazy.uibase.demo.core.style.annotation.Style
import com.eazy.uibase.demo.core.style.annotation.Title
import com.eazy.uibase.demo.databinding.DialogFragmentBinding
import com.eazy.uibase.widget.ZDialog
import com.eazy.uibase.widget.ZTipView

class ZDialogFragment : ComponentFragment<DialogFragmentBinding?, ZDialogFragment.Model?, ZDialogFragment.Styles?>()
    , ZDialog.DialogListener {

    class Model : ViewModel() {

        @Bindable
        val buttons: List<String> = listOf("删除内容", "列表标题")

        val buttons2: List<String>? = null
    }

    class Styles : ViewStyles() {

        @Bindable
        @Title("图片")
        @Description("附加在顶部的图片，资源ID类型")
        @Style(ContentStyle::class, params = ["image"])
        var image = 0

        @Bindable
        @Title("关闭按钮颜色")
        @Description("关闭按钮颜色，资源ID类型；设置为0，则不显示")
        @Style(ColorStyle::class, params = ["colorId"])
        var closeIconColor = 0

        @Bindable
        @Title("标题")
        @Description("标题文字，一般在中间显示")
        var title = "标题"

        @Bindable
        @Title("详细描述")
        @Description("描述文字，显示在标题下面")
        var subTitle = "真的要撤回该作业吗？\n" +
            "所有已提交的学生作业也会被删除！"

        @Bindable
        @Title("内容")
        @Description("中间或者整体内容，资源ID：布局（layout，中间内容）或者样式（style，整体内容）")
        @Style(ContentStyle::class, params = ["@layout"])
        var content = 0

        @Bindable
        @Title("确认按钮")
        @Description("底部操作按钮，可选，参见按纽的 content 样式")
        @Style(ContentStyle::class, params = ["<button>"])
        var confirmButton = R.string.confirm

        @Bindable
        @Title("取消按钮")
        @Description("底部操作按钮，可选，参见按纽的 content 样式")
        @Style(ContentStyle::class, params = ["<button>"])
        var cancelButton = R.string.cancel

        @Bindable
        var moreButtons = false

        @Bindable
        @Title("底部复选框")
        @Description("底部复选框，可选，资源ID：文字")
        @Style(ContentStyle::class, params = ["text"])
        var checkBox = 0
    }

    companion object {
        private const val TAG = "ZDialogFragment"
    }

    override fun backgroundColor(): Int {
        return R.color.blue_100
    }

    private var lp: ViewGroup.LayoutParams? = null

    var buttonClick = View.OnClickListener {
        val dialog = binding.dialog
        lp = dialog.layoutParams
        binding.frame.removeView(dialog)
        dialog.popUp(parentFragmentManager)
    }

    override fun dialogDismissed(dialog: ZDialog) {
        if (dialog.rootView == requireView().rootView) {
            return
        }
        dialog.post() {
            (dialog.parent as ViewGroup).removeView(dialog)
            binding.frame.addView(dialog, lp)
        }
    }

    override fun buttonClicked(dialog: ZDialog, btnId: Int) {
        val tip = ZTipView(requireContext())
        val name = if (btnId < 256) "index_$btnId" else resources.getResourceEntryName(btnId)
        tip.message = "点击了按钮: ${name}"
        tip.location = ZTipView.Location.AutoToast
        tip.popAt(dialog)
    }

}