package com.eazy.uibase.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.eazy.uibase.R
import com.eazy.uibase.dialog.adapter.OnSingleItemClickListener
import com.eazy.uibase.dialog.adapter.SingleSelectionAdapter
import com.eazy.uibase.dialog.bean.ItemBean
import com.eazy.uibase.utils.ScreenUtils
import com.eazy.uibase.utils.Strings
import kotlinx.android.synthetic.main.commonlib_dialog_single.*

class SingleSelectDialog : BaseDialogFragment() {

    private var mContext: Context? = null
    private var title: String? = null
    private var titleStyle: ContentStyle? = null
    private var adapter: SingleSelectionAdapter? = null
    private var dataList: ArrayList<ItemBean> = ArrayList()
    private var onConfirmListener: OnSingleSelectionConfirmListener? = null
    private var autoHeight = false

    //当前选中的一级选项
    private var currentFirstIndex = 0

    companion object {
        private const val TITLE = "TITLE"
        private const val TITLE_STYLE = "TITLE_STYLE"
        private const val CANCELABLE = "CANCELABLE"
        private const val DATA = "DATA"
        private const val AUTO_HEIGHT = "AUTO_HEIGHT"

        private fun newInstance(
            title: String?, cancelable: Boolean,
            data: ArrayList<ItemBean>, autoHeight: Boolean = false
        ): SingleSelectDialog {
            val bundle = Bundle()
            bundle.putString(TITLE, title)
            bundle.putSerializable(DATA, data)
            bundle.putBoolean(AUTO_HEIGHT, autoHeight)
            val dialog = SingleSelectDialog()
            dialog.arguments = bundle
            dialog.isCancelable = cancelable
            return dialog
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var cancelable = true
        arguments?.let {
            title = it.getString(TITLE)
            dataList = it.getSerializable(DATA) as ArrayList<ItemBean>
            cancelable = it.getBoolean(CANCELABLE, true)
            autoHeight = it.getBoolean(AUTO_HEIGHT)
        }

        val dialog = Dialog(activity!!)
        dialog.setCancelable(cancelable)
        dialog.setCanceledOnTouchOutside(cancelable)
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = context
        setStyle(STYLE_NO_TITLE, R.style.MyDialogFragment)
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        window?.let {
            val params = window.attributes
            params.gravity = Gravity.BOTTOM
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = if (autoHeight)
                WindowManager.LayoutParams.WRAP_CONTENT
            else
                ScreenUtils.dp2PxInt(context, 578f)
            window.attributes = params
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.commonlib_dialog_single, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        if (Strings.isNotBlank(title)) {
            tv_title.text = title
            titleStyle?.let { style ->
                if (style.isBold == 1) {
                    tv_title.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                }
                if (style.textColor > 0) {
                    context?.let { ContextCompat.getColor(it, style.textColor) }?.let { tv_title.setTextColor(it) }
                }
                tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, style.textSize)
            }
        }

        rl_close.setOnClickListener { dismissAllowingStateLoss() }

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager

        adapter = SingleSelectionAdapter(dataList, object : OnSingleItemClickListener {
            override fun onItemClick(position: Int) {
                resetFirstAdapter(position)
                recyclerView.postDelayed({
                    onConfirmListener?.onConfirm(getSelectMode())
                    dismissAllowingStateLoss()
                }, 200)

            }
        })
        recyclerView.adapter = adapter
        recyclerView.scrollToPosition(adapter?.getSelectedItemPosition() ?: 0)
    }


    private fun getSelectMode(): ItemBean? {
        if (dataList.isEmpty()) {
            return null
        }
        for (item in dataList) {
            if (item.isChecked) {
                return item
            }
        }
        return dataList[0]
    }

    fun resetFirstAdapter(position: Int) {
        if (position < dataList.size) {
            currentFirstIndex = position
            dataList[currentFirstIndex].isChecked = true
            for (index in dataList.indices) {
                if (index != currentFirstIndex) {
                    dataList[index].isChecked = false
                }
            }
            adapter?.notifyDataSetChanged()
        }
    }

    fun setConfirmListener(onConfirmListener: OnSingleSelectionConfirmListener?) {
        this.onConfirmListener = onConfirmListener
    }

    class Builder(context: Context) : AbsDialogBuilder<Builder>(context) {
        private var data1 = ArrayList<ItemBean>()
        private var onConfirmListener: OnSingleSelectionConfirmListener? = null
        private var autoHeight = false

        override fun getBuilderInstance(): Builder = this

        private var dialog: SingleSelectDialog? = null

        fun withData(data: ArrayList<ItemBean>): Builder {
            this.data1 = data
            return this
        }

        fun withConfirmListener(onConfirmListener: OnSingleSelectionConfirmListener): Builder {
            this.onConfirmListener = onConfirmListener
            return this
        }

        fun autoHeight(autoHeight: Boolean): Builder {
            this.autoHeight = autoHeight
            return this
        }

        fun show(fragmentManager: FragmentManager, tag: String) {
            if (dialog == null) {
                dialog = newInstance(title, cancelable, data1, autoHeight)
                dialog?.setConfirmListener(onConfirmListener)

            }
            dialog?.show(fragmentManager, tag)
        }
    }

}

interface OnSingleSelectionConfirmListener {
    fun onConfirm(mode: ItemBean?)

}
