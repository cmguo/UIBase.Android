package com.xhb.uibase.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.xhb.uibase.R
import com.xhb.uibase.utils.ScreenUtils
import com.xhb.uibase.utils.Strings
import kotlinx.android.synthetic.main.commonlib_dialog_common.*

class CommonDialog : DialogFragment() {
    private var iconResId: Int = 0

    //title
    private var title: String? = null
    private var titleStyle: ContentStyle? = null

    //content
    private var content: String? = null
    private var contentStyle: ContentStyle? = null

    //actions
    private var actions = ArrayList<DialogAction>()
    private var listeners = mutableMapOf<DialogAction, CommonActionListener>()
    private var onShowListener: DialogInterface.OnShowListener? = null
    private var onDismissListener: DialogInterface.OnDismissListener? = null
    private var repeatAction: DialogAction? = null
    private var hideClose = true

    companion object {
        private const val ICON_RES_ID = "ICON_RES_ID"
        private const val CANCELABLE = "CANCELABLE"
        private const val TITLE = "TITLE"
        private const val TITLE_STYLE = "TITLE_STYLE"
        private const val CONTENT = "CONTENT"
        private const val CONTENT_STYLE = "CONTENT_STYLE"
        private const val ACTIONS = "ACTIONS"
        private const val HIDE_CLOSE = "HIDE_CLOSE"

        private fun newInstance(
            iconResId: Int,
            title: String?,
            titleStyle: ContentStyle?,
            content: String?,
            contentStyle: ContentStyle?,
            actions: ArrayList<DialogAction>,
            cancelable: Boolean,
            hideClose: Boolean,
        ): CommonDialog {
            val bundle = Bundle()
            bundle.putInt(ICON_RES_ID, iconResId)
            bundle.putString(TITLE, title)
            bundle.putParcelable(TITLE_STYLE, titleStyle)
            bundle.putString(CONTENT, content)
            bundle.putParcelable(CONTENT_STYLE, contentStyle)
            bundle.putParcelableArrayList(ACTIONS, actions)
            bundle.putBoolean(CANCELABLE, cancelable)
            bundle.putBoolean(HIDE_CLOSE, hideClose)
            val dialog = CommonDialog()
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

        val dialog = Dialog(activity!!)
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.let {
            it.onDismiss(dialog)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            iconResId = it.getInt(ICON_RES_ID)
            title = it.getString(TITLE)
            titleStyle = it.getParcelable(TITLE_STYLE)
            content = it.getString(CONTENT)
            contentStyle = it.getParcelable(CONTENT_STYLE)
            actions = it.getParcelableArrayList(ACTIONS)!!
            hideClose = it.getBoolean(HIDE_CLOSE, true)
        }
        setStyle(STYLE_NO_TITLE, R.style.MyDialogFragment)
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        val params = window?.attributes
        params?.gravity = Gravity.CENTER
        params?.width = ScreenUtils.dp2PxInt(context, 296f)
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = params
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.commonlib_dialog_common, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        if (iconResId > 0) {
            dialog_icon.visibility = View.VISIBLE
            dialog_icon.setImageResource(iconResId)
        }
        if (Strings.isNotBlank(title)) {
            dialog_title.visibility = View.VISIBLE
            dialog_title.text = title
            titleStyle?.let { style ->
                if (style.isBold == 1) {
                    dialog_title.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                }
                if (style.textColor > 0) {
                    context?.let { ContextCompat.getColor(it, style.textColor) }?.let { dialog_title.setTextColor(it) }
                }
                if (style.gravity > 0) {
                    dialog_title.gravity = style.gravity
                }
                dialog_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, style.textSize)
            }
        }

        if (Strings.isNotBlank(content)) {
            dialog_content.text = content
            contentStyle?.let { style ->
                if (style.isBold == 1) {
                    dialog_content.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                }
                if (style.textColor > 0) {
                    context?.let { ContextCompat.getColor(it, style.textColor) }?.let { dialog_content.setTextColor(it) }
                }
                if (style.gravity > 0) {
                    dialog_content.gravity = style.gravity
                }
                dialog_content.setTextSize(TypedValue.COMPLEX_UNIT_SP, style.textSize)
            }
        }

        if (actions.size > 1) {
            setupActionButton(action1, actions[0])
            setupActionButton(action2, actions[1])
        } else if (actions.isNotEmpty()) {
            //单个按钮显示黄色按钮
            setupActionButton(action2, actions[0])
        }

        repeatAction?.let {
            dialog_repeat.visibility = View.VISIBLE
            repeatSelect.text = it.actionText
            it.contentStyle?.let { style ->
                if (style.isBold == 1) {
                    repeatSelect.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                }
                if (style.textColor > 0) {
                    context?.let { ContextCompat.getColor(it, style.textColor) }?.let { repeatSelect.setTextColor(it) }
                }
                if (style.gravity > 0) {
                    (repeatSelect.layoutParams as LinearLayout.LayoutParams).gravity = style.gravity
                }
                repeatSelect.setTextSize(TypedValue.COMPLEX_UNIT_SP, style.textSize)
            }
            repeatSelect.setOnCheckedChangeListener { _, _ ->
                listeners[it]?.onClick(this)
            }
        }

        dialog_close.visibility = if (hideClose) View.GONE else View.VISIBLE
        dialog_close.setOnClickListener { dismiss() }
    }

    private fun setupActionButton(actionButton: TextView, dialogAction: DialogAction) {
        actionButton.visibility = View.VISIBLE
        actionButton.text = dialogAction.actionText
        dialogAction.contentStyle?.let { style ->
            if (style.isBold == 1) {
                actionButton.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            }
            if (style.textColor > 0) {
                context?.let { ContextCompat.getColor(it, style.textColor) }?.let { actionButton.setTextColor(it) }
            }
            if (style.gravity > 0) {
                actionButton.gravity = style.gravity
            }
            actionButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, style.textSize)
        }
        dialogAction.componentKey?.let { key ->
            actionButton.contentDescription = key
        }
        actionButton.setOnClickListener {
            listeners[dialogAction]?.onClick(this)
        }
    }

    fun setOnShowListener(listener: DialogInterface.OnShowListener?) {
        this.onShowListener = listener
    }

    fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        this.onDismissListener = listener
    }

    fun setListeners(listeners: MutableMap<DialogAction, CommonActionListener>) {
        this.listeners = listeners
    }

    fun setRepeat(repeatAction: DialogAction?) {
        this.repeatAction = repeatAction
    }

    fun getRepeatState(): Boolean = repeatSelect.isChecked

    override fun show(manager: FragmentManager, tag: String?) {
        if (!isAdded && !isVisible && !isRemoving) {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
            try {
                manager.executePendingTransactions()
            } catch (e: Exception) {
                // ignore
                if (e.message == "Fragment host has been destroyed") {
                    return
                }

                throw e
            }
        }
    }

    override fun dismiss() {
        if (isAdded) {
            dismissAllowingStateLoss()
            try {
                fragmentManager?.executePendingTransactions()
            } catch (e: Exception) {
                // ignore
                if (e.message == "Fragment host has been destroyed") {
                    return
                }

                throw e
            }
        }
    }

    class Builder(context: Context) : AbsDialogBuilder<Builder>(context) {

        override fun getBuilderInstance(): Builder = this

        //icon
        private var iconResId: Int = 0

        //content
        private var content: String? = null
        private var contentStyle: ContentStyle? = null

        private var actions = ArrayList<DialogAction>()
        private var listeners = mutableMapOf<DialogAction, CommonActionListener>()
        private var dialog: CommonDialog? = null
        private var repeatAction: DialogAction? = null
        private var hideClose = true
        private var canceledOnTouchOutside = true

        fun withIcon(@DrawableRes iconResId: Int): Builder {
            this.iconResId = iconResId
            return this
        }

        fun withContent(@StringRes contentResId: Int): Builder {
            return this.withContent(context.resources.getString(contentResId), null)
        }

        fun withContent(@StringRes contentResId: Int, contentStyle: ContentStyle? = null): Builder {
            return this.withContent(context.resources.getString(contentResId), contentStyle)
        }

        fun withContent(content: String): Builder {
            return this.withContent(content, null)
        }

        fun withContent(content: String, @Nullable contentStyle: ContentStyle? = null): Builder {
            this.content = content
            this.contentStyle = contentStyle
            return this
        }

        fun withAction(
            actionBtnText: String,
            listener: CommonActionListener,
            actionStyle: ContentStyle? = null,
        ): Builder {
            return this.withAction(actionBtnText, listener, null, actionStyle)
        }

        fun withAction(
            @StringRes actionBtnTextResId: Int,
            listener: CommonActionListener,
            actionStyle: ContentStyle? = null,
        ): Builder {
            return this.withAction(context.resources.getString(actionBtnTextResId), listener, null, actionStyle)
        }

        fun withRepeatAction(
            @StringRes actionBtnTextResId: Int,
            listener: CommonActionListener? = null,
            actionStyle: ContentStyle? = null,
        ): Builder {
            return this.withRepeatAction(
                context.resources.getString(actionBtnTextResId),
                listener,
                null,
                actionStyle
            )
        }

        fun withRepeatAction(
            actionBtnText: String,
            listener: CommonActionListener? = null,
            actionStyle: ContentStyle? = null,
        ): Builder {
            return this.withRepeatAction(actionBtnText, listener, null, actionStyle)
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
            listener: CommonActionListener,
            @StringRes componentKeyId: Int?,
            actionStyle: ContentStyle? = null,
        ): Builder {
            var componentKey: String? = null
            if (componentKeyId != null) {
                componentKey = context.resources.getString(componentKeyId)
            }
            val action = DialogAction(actionBtnText, actionStyle, componentKey)
            actions.add(action)
            listeners[action] = listener
            return this
        }

        fun withAction(
            actionBtnTextResId: Int,
            listener: CommonActionListener,
            @StringRes componentKeyId: Int?,
            actionStyle: ContentStyle? = null,
        ): Builder {
            return this.withAction(context.resources.getString(actionBtnTextResId), listener, componentKeyId, actionStyle)
        }

        private fun withRepeatAction(
            actionBtnText: String,
            listener: CommonActionListener? = null,
            @StringRes componentKeyId: Int?,
            actionStyle: ContentStyle? = null,
        ): Builder {
            var componentKey: String? = null
            if (componentKeyId != null) {
                componentKey = context.resources.getString(componentKeyId)
            }
            repeatAction = DialogAction(actionBtnText, actionStyle, componentKey).apply {
                listener?.let {
                    listeners[this] = it
                }
            }
            return this
        }

        /**
         * 显示关闭按钮，默认是隐藏的
         */
        fun showCloseButton(): Builder {
            hideClose = false
            return this
        }

        /**
         * 点击周边是否消失,默认是消失的
         */
        fun setCanceledOnTouchOutside(cancelable: Boolean): Builder {
            canceledOnTouchOutside = cancelable
            return this
        }

        fun show(fragmentManager: FragmentManager, tag: String) {
            if (dialog == null) {
                dialog = newInstance(iconResId, title, titleStyle, content, contentStyle, actions,
                    cancelable, hideClose)
                dialog?.setListeners(listeners)
                dialog?.setRepeat(repeatAction)
                dialog?.setOnShowListener(onShowListener)
                dialog?.setOnDismissListener(onDismissListener)
            }
            dialog?.show(fragmentManager, tag)
            dialog?.dialog?.setCanceledOnTouchOutside(canceledOnTouchOutside)
        }

        fun dismiss() {
            dialog?.dismiss()
        }
    }
}

interface CommonActionListener {
    fun onClick(dialog: CommonDialog)
}
