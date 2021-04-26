package com.eazy.uibase.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.eazy.uibase.R
import com.eazy.uibase.utils.ScreenUtils
import com.eazy.uibase.utils.Strings
import kotlinx.android.synthetic.main.commonlib_dialog_bottom.*

class BottomDialog : DialogFragment() {
    //title
    private var title: String? = null

    //actions
    private var actions = ArrayList<DialogAction>()
    private var listeners = ArrayList<BottomActionListener>()
    private var onShowListener: DialogInterface.OnShowListener? = null
    private var onDismissListener: DialogInterface.OnDismissListener? = null

    //show cancel button
    private var showCancel: Boolean = true

    companion object {
        private const val CANCELABLE = "CANCELABLE"
        private const val TITLE = "TITLE"
        private const val ACTIONS = "ACTIONS"
        private const val SHOW_CANCEL = "SHOW_CANCEL"

        private fun newInstance(
            title: String?,
            actions: ArrayList<DialogAction>,
            cancelable: Boolean,
            showCancel: Boolean
        ): BottomDialog {
            val bundle = Bundle()
            bundle.putString(TITLE, title)
            bundle.putParcelableArrayList(ACTIONS, actions)
            bundle.putBoolean(CANCELABLE, cancelable)
            bundle.putBoolean(SHOW_CANCEL, showCancel)
            val dialog = BottomDialog()
            dialog.arguments = bundle
            dialog.isCancelable = cancelable
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var cancelable = true
        arguments?.let {
            cancelable = it.getBoolean(CANCELABLE, true)
        }

        val dialog = Dialog(context!!)
        dialog.setCancelable(cancelable)
        dialog.setCanceledOnTouchOutside(cancelable)
        onShowListener?.let {
            dialog.setOnShowListener(it)
        }
        onDismissListener?.let {
            dialog.setOnDismissListener(it)
        }
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(TITLE)
            actions = it.getParcelableArrayList(ACTIONS)!!
            showCancel = it.getBoolean(SHOW_CANCEL)
        }
        setStyle(STYLE_NO_TITLE, R.style.MyDialogFragment)
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        val params = window?.attributes
        params?.gravity = Gravity.BOTTOM
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = params
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.commonlib_dialog_bottom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        if (Strings.isNotBlank(title)) {
            dialog_title.visibility = View.VISIBLE
            dialog_title.text = title
        }

        val size = actions.size
        actions.forEachIndexed { index, dialogAction ->
            ll_container.addView(generateItemView(dialogAction, index == size - 1, index))
        }
    }

    private fun generateItemView(action: DialogAction, isLastItem: Boolean, index: Int): TextView {
        val textView = TextView(context)
        action.componentKey?.let {
            textView.contentDescription = it
        }
        textView.text = action.actionText
        if (Strings.isNullOrEmpty(title) && index == 0) {
            textView.background = ContextCompat.getDrawable(context!!, R.drawable.commonlib_top_radius_bg)
        } else {
            textView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.bluegrey_00))
        }

        val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        if (showCancel && isLastItem) {
            param.topMargin = ScreenUtils.dp2PxInt(context, 7f)
        }
        param.bottomMargin = ScreenUtils.dp2PxInt(context, 1f)
        textView.layoutParams = param
        val padding = ScreenUtils.dp2PxInt(context, 14f)
        textView.setPadding(padding, padding, padding, padding)
        action.contentStyle?.let { style ->
            textView.setTextColor(ContextCompat.getColor(context!!, style.textColor))
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, style.textSize)
        } ?: kotlin.run {
            textView.setTextColor(ContextCompat.getColor(context!!, R.color.bluegrey_900))
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        }
        textView.gravity = Gravity.CENTER
        textView.setOnClickListener {
            listeners[index].onClick(this)
        }
        return textView
    }

    fun setListeners(listeners: ArrayList<BottomActionListener>) {
        this.listeners = listeners
    }

    fun setOnShowListener(listener: DialogInterface.OnShowListener?) {
        this.onShowListener = listener
    }

    fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        this.onDismissListener = listener
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!isAdded && !isVisible && !isRemoving) {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        }
    }

    override fun dismiss() {
        dismissAllowingStateLoss()
    }

    class Builder(context: Context) : AbsDialogBuilder<Builder>(context) {

        override fun getBuilderInstance(): Builder = this

        //show cancel button
        private var showCancel = true
        private var actions = ArrayList<DialogAction>()
        private var listeners = ArrayList<BottomActionListener>()

        private var dialog: BottomDialog? = null

        /**
         * 添加普通类型的操作按钮
         *
         * @param actionBtnText   文案
         * @param listener           点击回调事件
         * @param actionStyle        文字样式
         */
        fun withAction(
            actionBtnText: String,
            listener: BottomActionListener,
            @Nullable actionStyle: ContentStyle? = null
        ): Builder {
            return this.withAction(actionBtnText, listener, null, actionStyle)
        }

        fun withAction(
            actionBtnTextResId: Int,
            listener: BottomActionListener,
            @Nullable actionStyle: ContentStyle? = null
        ): Builder {
            return this.withAction(context.resources.getString(actionBtnTextResId), listener, null, actionStyle)
        }

        /**
         * 添加普通类型的操作按钮
         *
         * @param actionBtnText   文案
         * @param listener           点击回调事件
         * @param componentKeyId       埋点key
         * @param actionStyle        文字样式
         */
        fun withAction(
            actionBtnText: String,
            listener: BottomActionListener,
            @StringRes componentKeyId: Int?,
            @Nullable actionStyle: ContentStyle? = null
        ): Builder {
            var componentKey: String? = null
            if (componentKeyId != null) {
                componentKey = context.resources.getString(componentKeyId)
            }
            val action = DialogAction(actionBtnText, actionStyle, componentKey)
            actions.add(action)
            listeners.add(listener)
            return this
        }

        fun withAction(
            actionBtnTextResId: Int,
            listener: BottomActionListener,
            @StringRes componentKeyId: Int?,
            @Nullable actionStyle: ContentStyle? = null
        ): Builder {
            return this.withAction(context.resources.getString(actionBtnTextResId), listener, componentKeyId, actionStyle)
        }


        fun showCancelBtn(showCancel: Boolean): Builder {
            this.showCancel = showCancel
            return this
        }

        fun show(fragmentManager: FragmentManager, tag: String) {
            if (dialog == null) {
                dialog = newInstance(title, actions, cancelable, showCancel)
                dialog?.setListeners(listeners)
                dialog?.setOnShowListener(onShowListener)
                dialog?.setOnDismissListener(onDismissListener)
            }
            dialog?.show(fragmentManager, tag)
        }
    }
}

interface BottomActionListener {
    fun onClick(dialog: BottomDialog)
}
