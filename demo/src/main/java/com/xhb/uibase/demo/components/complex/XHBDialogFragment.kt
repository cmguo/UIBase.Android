package com.xhb.uibase.demo.components.complex

import android.content.Context
import android.os.Bundle
import android.view.View
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.ViewModel
import com.xhb.uibase.demo.core.ViewStyles
import com.xhb.uibase.demo.databinding.XhbDialogFragmentBinding
import com.xhb.uibase.dialog.*
import com.xhb.uibase.dialog.bean.ItemBean
import kotlinx.android.synthetic.main.xhb_dialog_fragment.*

class XHBDialogFragment : ComponentFragment<XhbDialogFragmentBinding?, XHBDialogFragment.Model?, XHBDialogFragment.Styles?>() {

    class Model : ViewModel()

    class Styles : ViewStyles()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { initView(it) }
    }

    private fun initView(context: Context) {
        btn_common_dialog.setOnClickListener {
            val builder = CommonDialog.Builder(context)
            builder.withTitle("Common Dialog title")
                .withContent("Common dialog content.")
                .withAction("确定", object : CommonActionListener {
                    override fun onClick(dialog: CommonDialog) {
                        dialog.dismiss()
                    }

                })
                .withAction("取消", object : CommonActionListener {
                    override fun onClick(dialog: CommonDialog) {
                        dialog.dismiss()
                    }
                })
                .show(childFragmentManager, "commonDialog")
        }

        btn_input_dialog.setOnClickListener {
            val builder = InputDialog.Builder(context)
            builder.withTitle("请给您的账号设置一个密码吧")
                .withSubTitle("8—16位，且必须包含大小写字母、数字和符号中的三种组合")
                .withHint("请输入密码")
                .withPasswordTransformationMethod(true)
                .withShowPasswordSwitch(true)
                .withAction("取消", object : InputActionListener {
                    override fun onClick(dialog: InputDialog, result: String) {
                        dialog.dismiss()
                    }
                })
                .withAction("设置", object : InputActionListener {
                    override fun onClick(dialog: InputDialog, result: String) {
                        dialog.dismiss()
                    }
                })
                .show(childFragmentManager, "input dialog")
        }

        btn_single_dialog.setOnClickListener {
            val builder = SingleSelectDialog.Builder(context)
            builder.withTitle("选择学科")
                .withData(getSubjectData())
                .withConfirmListener(object : OnSingleSelectionConfirmListener {
                    override fun onConfirm(mode: ItemBean?) {
                    }

                })
                .setCancelable(true)
                .show(childFragmentManager, "single choose dialog")
        }

        btn_bottom_dialog.setOnClickListener {
            BottomDialog.Builder(context)
                .withAction("QQ邀请", object : BottomActionListener {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                    }
                })
                .withAction("通讯录邀请", object : BottomActionListener {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                    }
                })
                .withAction("手机号邀请", object : BottomActionListener {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()

                    }
                })
                .withAction("取消", object : BottomActionListener {
                    override fun onClick(dialog: BottomDialog) {
                        dialog.dismiss()
                    }
                })
                .show(childFragmentManager, "otherInviteDialog")
        }
    }

    private fun getSubjectData(): ArrayList<ItemBean> {
        val list = ArrayList<ItemBean>()
        list.add(ItemBean("语文", true))
        list.add(ItemBean("数学"))
        list.add(ItemBean("英语"))
        list.add(ItemBean("物理"))
        list.add(ItemBean("化学"))
        return list
    }

    companion object {
        private const val TAG = "XHBDialogFragment"
    }
}