package com.xhb.uibase.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.xhb.uibase.R
import com.xhb.uibase.utils.CommonUtil
import com.xhb.uibase.utils.ScreenUtils
import com.xhb.uibase.utils.Strings
import kotlinx.android.synthetic.main.commonlib_dialog_input.*

class InputDialog : DialogFragment() {
    private var iconResId: Int = 0

    //title
    private var title: String? = null
    private var titleStyle: ContentStyle? = null
    private var subTitle: String? = null
    private var subTitleStyle: ContentStyle? = null

    //hint
    private var hintContent: String? = null
    private var hintStyle: ContentStyle? = null

    //edt content
    private var edtContent: String? = null
    private var edtContentStyle: ContentStyle? = null
    private var maxLength: Int = -1
    private var isShowCharNum = false
    private var maxLines: Int = -1
    private var edtHeight: Int = -1
    private var inputType: Int = InputType.TYPE_CLASS_TEXT
    private var isPasswordTransformationMethod: Boolean = false
    private var isShowPasswordSwitch: Boolean = false

    //actions
    private var actions = ArrayList<DialogAction>()
    private var listeners = ArrayList<InputActionListener>()
    private var onShowListener: DialogInterface.OnShowListener? = null
    private var onDismissListener: DialogInterface.OnDismissListener? = null
    private var hideClose = true

    companion object {
        private const val ICON_RES_ID = "ICON_RES_ID"
        private const val CANCELABLE = "CANCELABLE"
        private const val TITLE = "TITLE"
        private const val TITLE_STYLE = "TITLE_STYLE"
        private const val SUB_TITLE = "SUB_TITLE"
        private const val SUB_TITLE_STYLE = "SUB_TITLE_STYLE"
        private const val HINT_CONTENT = "HINT_CONTENT"
        private const val HINT_STYLE = "HINT_STYLE"
        private const val EDT_CONTENT = "EDT_CONTENT"
        private const val EDT_CONTENT_STYLE = "EDT_CONTENT_STYLE"
        private const val ACTIONS = "ACTIONS"
        private const val MAX_LENGTH = "MAX_LENGTH"
        private const val IS_SHOW_CHAR_NUM = "IS_SHOW_CHAR_NUM"
        private const val MAX_LINE = "MAX_LINE"
        private const val EDT_HEIGHT = "EDT_HEIGHT"
        private const val INPUT_TYPE = "INPUT_TYPE"
        private const val IS_PASSWORD_TRANSFORMATION_METHOD = "IS_PASSWORD_TRANSFORMATION_METHOD"
        private const val IS_SHOW_PASSWORD_SWITCH = "IS_SHOW_PASSWORD_SWITCH"
        private const val HIDE_CLOSE = "HIDE_CLOSE"

        private fun newInstance(
            iconResId: Int,
            title: String?,
            titleStyle: ContentStyle?,
            subTitle: String?,
            subTitleStyle: ContentStyle?,
            hintContent: String?,
            hintStyle: ContentStyle?,
            edtContent: String?,
            edtContentStyle: ContentStyle?,
            actions: ArrayList<DialogAction>,
            cancelable: Boolean,
            maxLength: Int,
            isShowCharNum: Boolean,
            maxLines: Int,
            edtHeight: Int,
            inputType: Int,
            isPasswordTransformationMethod: Boolean,
            isShowPasswordSwitch: Boolean,
            hideClose: Boolean,
        ): InputDialog {
            val bundle = Bundle()
            bundle.putInt(ICON_RES_ID, iconResId)
            bundle.putString(TITLE, title)
            bundle.putParcelable(TITLE_STYLE, titleStyle)
            bundle.putString(SUB_TITLE, subTitle)
            bundle.putParcelable(SUB_TITLE_STYLE, subTitleStyle)
            bundle.putString(HINT_CONTENT, hintContent)
            bundle.putParcelable(HINT_STYLE, hintStyle)
            bundle.putString(EDT_CONTENT, edtContent)
            bundle.putParcelable(EDT_CONTENT_STYLE, edtContentStyle)
            bundle.putParcelableArrayList(ACTIONS, actions)
            bundle.putBoolean(CANCELABLE, cancelable)
            bundle.putInt(MAX_LENGTH, maxLength)
            bundle.putBoolean(IS_SHOW_CHAR_NUM, isShowCharNum)
            bundle.putInt(MAX_LINE, maxLines)
            bundle.putInt(EDT_HEIGHT, edtHeight)
            bundle.putInt(INPUT_TYPE, inputType)
            bundle.putBoolean(IS_PASSWORD_TRANSFORMATION_METHOD, isPasswordTransformationMethod)
            bundle.putBoolean(IS_SHOW_PASSWORD_SWITCH, isShowPasswordSwitch)
            bundle.putBoolean(HIDE_CLOSE, hideClose)
            val dialog = InputDialog()
            dialog.arguments = bundle
            dialog.isCancelable = cancelable
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            iconResId = it.getInt(ICON_RES_ID)
            title = it.getString(TITLE)
            titleStyle = it.getParcelable(TITLE_STYLE)
            subTitle = it.getString(SUB_TITLE)
            subTitleStyle = it.getParcelable(SUB_TITLE_STYLE)
            hintContent = it.getString(HINT_CONTENT)
            hintStyle = it.getParcelable(HINT_STYLE)
            edtContent = it.getString(EDT_CONTENT)
            edtContentStyle = it.getParcelable(EDT_CONTENT_STYLE)
            actions = it.getParcelableArrayList(ACTIONS)!!
            maxLength = it.getInt(MAX_LENGTH)
            isShowCharNum = it.getBoolean(IS_SHOW_CHAR_NUM)
            maxLines = it.getInt(MAX_LINE)
            edtHeight = it.getInt(EDT_HEIGHT)
            inputType = it.getInt(INPUT_TYPE)
            isPasswordTransformationMethod = it.getBoolean(IS_PASSWORD_TRANSFORMATION_METHOD)
            isShowPasswordSwitch = it.getBoolean(IS_SHOW_PASSWORD_SWITCH)
            hideClose = it.getBoolean(HIDE_CLOSE, true)
        }
        setStyle(STYLE_NO_TITLE, R.style.MyDialogFragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return getInitializationDialog()
    }

    private fun getInitializationDialog(): Dialog {
        var cancelable = true
        arguments?.let {
            cancelable = it.getBoolean(CANCELABLE, true)
        }
        val dialog = object : Dialog(activity!!, theme) {
            override fun cancel() {
                view?.let { view ->
                    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                super.cancel()
            }
        }
        dialog.setCancelable(cancelable)
        dialog.setCanceledOnTouchOutside(cancelable)
        dialog.setOnShowListener { listenerDialog ->
            onShowListener?.onShow(listenerDialog)
        }
        onDismissListener?.let {
            dialog.setOnDismissListener(it)
        }
        return dialog
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
        return inflater.inflate(R.layout.commonlib_dialog_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onResume() {
        super.onResume()
        dialog_edt?.apply {
            post {
                requestFocus()
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }
        }
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
                dialog_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, style.textSize)
            }
        }
        if (Strings.isNotBlank(subTitle)) {
            dialog_sub_title.visibility = View.VISIBLE
            dialog_sub_title.text = subTitle
            subTitleStyle?.let { style ->
                if (style.isBold == 1) {
                    dialog_sub_title.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                }
                if (style.textColor > 0) {
                    context?.let { ContextCompat.getColor(it, style.textColor) }?.let { dialog_sub_title.setTextColor(it) }
                }
                dialog_sub_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, style.textSize)
            }
        }
        floatInputLayout.setMaxCounter(maxLength)
        if (!isShowCharNum) {
            floatInputLayout.setCounterEnabled(false)
        }
        if (maxLength > 0) {
            dialog_edt.maxEms = maxLength
        } else {
            dialog_edt.maxEms = Int.MAX_VALUE
        }
        if (maxLines > 0) {
            dialog_edt.maxLines = maxLines
        }
        dialog_edt.inputType = inputType
        if (Strings.isNotBlank(edtContent)) {
            dialog_edt.setText(edtContent)
            dialog_edt.setSelection((dialog_edt.text ?: "").toString().length)
            edtContentStyle?.let { style ->
                if (style.isBold == 1) {
                    dialog_edt.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                }
                if (style.textColor > 0) {
                    context?.let { ContextCompat.getColor(it, style.textColor) }?.let { dialog_edt.setTextColor(it) }
                }
                dialog_edt.setTextSize(TypedValue.COMPLEX_UNIT_SP, style.textSize)
                if (style.maxEms > -1) {
                    dialog_edt.filters = arrayOf(
                        InputFilter.LengthFilter(style.maxEms)
                    )
                }
            }
        }
        if (edtHeight > 0) {
            dialog_edt.height = ScreenUtils.dp2PxInt(context, edtHeight.toFloat())
        }
        if (Strings.isNotBlank(hintContent)) {
            dialog_edt.hint = hintContent
            hintStyle?.let { style ->
                dialog_edt.setHintTextColor(style.textColor)
            }
        }

        if (actions.size > 1) {
            setupActionButton(action1, 0)
            setupActionButton(action2, 1)
        } else if (actions.isNotEmpty()) {
            //单个按钮显示黄色按钮
            setupActionButton(action2, 0)
        }

        if (maxLength != -1) {
            CommonUtil.setMaxLengthForEditText(dialog_edt, maxLength)
        }

        if (isPasswordTransformationMethod) {
            dialog_edt.transformationMethod = PasswordTransformationMethod.getInstance()
        }

        if (isShowPasswordSwitch) {
            cb_pwd_display.visibility = View.VISIBLE
            cb_pwd_display.setOnClickListener {
                CommonUtil.setPasswordTransformationToEditText(dialog_edt, !cb_pwd_display.isChecked)
            }
        }

        dialog_close.visibility = if (hideClose) View.GONE else View.VISIBLE
        dialog_close.setOnClickListener { dismiss() }
    }

    private fun setupActionButton(actionButton: TextView, index: Int) {
        val dialogAction = actions[index]
        actionButton.visibility = View.VISIBLE
        actionButton.text = dialogAction.actionText
        dialogAction.contentStyle?.let { style ->
            if (style.isBold == 1) {
                actionButton.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            }
            if (style.textColor > 0) {
                actionButton.setTextColor(style.textColor)
            }
        }
        actionButton.setOnClickListener {
            CommonUtil.hideKeyBoard(context, dialog_edt)
            listeners[index].onClick(this, dialog_edt.text.toString().trim())
        }
        dialogAction.componentKey?.let { key ->
            actionButton.contentDescription = key
        }
    }

    fun setListeners(listeners: ArrayList<InputActionListener>) {
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

    fun getEditText(): EditText? = dialog?.dialog_edt

    class Builder(context: Context) : AbsDialogBuilder<Builder>(context) {
        override fun getBuilderInstance(): Builder = this

        //icon
        private var iconResId: Int = 0
        private var subTitle: String? = null
        private var subTitleStyle: ContentStyle? = null

        //edt content
        private var edtContent: String? = null
        private var edtContentStyle: ContentStyle? = null
        private var maxLength: Int = -1
        private var isShowCharNum = false   // 是否显示，输入的文字个数
        private var maxLines: Int = -1
        private var inputType: Int = InputType.TYPE_CLASS_TEXT
        private var edtHeight: Int = -1
        private var isPasswordTransformationMethod = false
        private var isShowPasswordSwitch = false

        //hint
        private var hintContent: String? = null
        private var hintStyle: ContentStyle? = null

        private var actions = ArrayList<DialogAction>()
        private var listeners = ArrayList<InputActionListener>()
        private var hideClose = true

        private var dialog: InputDialog? = null

        fun withIcon(@DrawableRes iconResId: Int): Builder {
            this.iconResId = iconResId
            return this
        }

        fun withSubTitle(@StringRes subTitleResId: Int): Builder {
            return this.withSubTitle(context.resources.getString(subTitleResId), null)
        }

        fun withSubTitle(@StringRes subTitleResId: Int, style: ContentStyle? = null): Builder {
            return this.withSubTitle(context.resources.getString(subTitleResId), style)
        }

        fun withSubTitle(@NonNull subTitle: String): Builder {
            return this.withSubTitle(subTitle, null)
        }

        fun withSubTitle(@NonNull subTitle: String, style: ContentStyle? = null): Builder {
            this.subTitle = subTitle
            this.subTitleStyle = style
            return this
        }

        fun withEdtContent(@StringRes edtTextId: Int): Builder {
            return this.withEdtContent(context.resources.getString(edtTextId), null)
        }

        fun withEdtContent(@StringRes edtTextId: Int, style: ContentStyle? = null): Builder {
            return this.withEdtContent(context.resources.getString(edtTextId), style)
        }

        fun withEdtContent(@NonNull edtText: String): Builder {
            return this.withEdtContent(edtText, null)
        }

        fun withEdtContent(@NonNull edtText: String, style: ContentStyle? = null): Builder {
            this.edtContent = edtText
            this.edtContentStyle = style
            return this
        }

        fun withHint(@StringRes hintResId: Int, hintStyle: ContentStyle? = null): Builder {
            return this.withHint(context.resources.getString(hintResId), hintStyle)
        }

        fun withHint(@NonNull hint: String, hintStyle: ContentStyle? = null): Builder {
            this.hintContent = hint
            this.hintStyle = hintStyle
            return this
        }

        fun withEdtMaxLength(length: Int, isShowCharNum: Boolean = false): Builder {
            this.isShowCharNum = isShowCharNum
            this.maxLength = length
            return this
        }

        fun withMaxLines(maxLines: Int): Builder {
            this.maxLines = maxLines
            return this
        }

        fun withInputType(inputType: Int): Builder {
            this.inputType = inputType
            return this
        }

        fun withEdtHeight(height: Int): Builder {
            this.edtHeight = height
            return this
        }

        fun withPasswordTransformationMethod(isPasswordTransformationMethod: Boolean): Builder {
            this.isPasswordTransformationMethod = isPasswordTransformationMethod
            return this
        }

        fun withShowPasswordSwitch(isShowPasswordSwitch: Boolean): Builder {
            this.isShowPasswordSwitch = isShowPasswordSwitch
            return this
        }

        /**
         * 添加普通类型的操作按钮
         *
         * @param actionBtnText   文案
         * @param listener           点击回调事件
         * @param actionStyle        文字样式
         */
        fun withAction(
            actionBtnText: String,
            listener: InputActionListener,
            actionStyle: ContentStyle? = null,
        ): Builder {
            return this.withAction(actionBtnText, listener, null, actionStyle)
        }

        fun withAction(
            @StringRes actionBtnTextResId: Int,
            listener: InputActionListener,
            actionStyle: ContentStyle? = null,
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
            listener: InputActionListener,
            @StringRes componentKeyId: Int?,
            actionStyle: ContentStyle? = null,
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
            listener: InputActionListener,
            @StringRes componentKeyId: Int?,
            actionStyle: ContentStyle? = null,
        ): Builder {
            return this.withAction(context.resources.getString(actionBtnTextResId), listener, componentKeyId, actionStyle)
        }

        /**
         * 显示关闭按钮，默认是隐藏的
         */
        fun showCloseButton(): Builder {
            hideClose = false
            return this
        }

        fun getEditText(): EditText? {
            if (dialog != null) {
                return dialog?.getEditText()
            }
            return null
        }

        fun getDialog(): Dialog? = dialog?.dialog

        fun show(fragmentManager: FragmentManager, tag: String) {
            if (dialog == null) {
                dialog = newInstance(iconResId, title, titleStyle, subTitle, subTitleStyle,
                    hintContent, hintStyle, edtContent, edtContentStyle, actions, cancelable,
                    maxLength, isShowCharNum, maxLines, edtHeight, inputType,
                    isPasswordTransformationMethod, isShowPasswordSwitch, hideClose)
                dialog?.setListeners(listeners)
                dialog?.setOnShowListener(onShowListener)
                dialog?.setOnDismissListener(onDismissListener)
            }
            dialog?.show(fragmentManager, tag)
        }

        fun dismiss() {
            dialog?.dismiss()
        }
    }
}

interface InputActionListener {
    fun onClick(dialog: InputDialog, result: String)
}
