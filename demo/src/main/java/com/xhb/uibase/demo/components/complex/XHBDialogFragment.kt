package com.xhb.uibase.demo.components.complex

import android.content.Context
import android.os.Bundle
import android.view.View
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.SkinManager
import com.xhb.uibase.demo.core.ViewModel
import com.xhb.uibase.demo.core.ViewStyles
import com.xhb.uibase.demo.databinding.XhbDialogFragmentBinding
import com.xhb.uibase.dialog.CommonActionListener
import com.xhb.uibase.dialog.CommonDialog
import com.xhb.uibase.dialog.InputActionListener
import com.xhb.uibase.dialog.InputDialog
import kotlinx.android.synthetic.main.xhb_dialog_fragment.*
import skin.support.observe.SkinObservable
import skin.support.observe.SkinObserver

class XHBDialogFragment : ComponentFragment<XhbDialogFragmentBinding?, XHBDialogFragment.Model?, XHBDialogFragment.Styles?>(), SkinObserver {

    class Model(fragment: XHBDialogFragment?) : ViewModel()

    class Styles(fragment: XHBDialogFragment?) : ViewStyles()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SkinManager.addObserver(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { initView(it) }
    }

    override fun onDestroy() {
        SkinManager.removeObserver(this)
        super.onDestroy()
    }

    override fun updateSkin(observable: SkinObservable, o: Any) {
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
    }

    companion object {
        private const val TAG = "XHBDialogFragment"
    }
}