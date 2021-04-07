package com.xhb.uibase.widget

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xhb.uibase.R
import com.xhb.uibase.view.list.BaseItemBinding
import com.xhb.uibase.view.list.DividerDecoration
import com.xhb.uibase.view.list.RecyclerViewAdapter
import kotlin.collections.ArrayList

class XHBActionSheet @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : FrameLayout(context, attrs, R.attr.actionSheetStyle) {

    @FunctionalInterface
    interface ActionSheetListener {
        fun onAction(sheet: XHBActionSheet, index: Int)
    }

    @DrawableRes
    var icon: Int = 0
        set(value) {
            if (field == value) return
            field = value
            if (value == 0) {
                _imageView.visibility = View.GONE
            } else {
                _imageView.setImageDrawable(ContextCompat.getDrawable(context, value))
                _imageView.visibility = View.VISIBLE
            }
        }

    var title: CharSequence?
        get() = _textView.text
        set(value) {
            _textView.text = value
            if (value == null || value.isEmpty()) {
                _textView.visibility = View.GONE
            } else {
                _textView.visibility = View.VISIBLE
            }
        }

    var subTitle: CharSequence?
        get() = _textView2.text
        set(value) {
            _textView2.text = value
            if (value == null || value.isEmpty()) {
                _textView2.visibility = View.GONE
            } else {
                _textView2.visibility = View.VISIBLE
            }
        }

    var titles: Iterable<Any> = ArrayList()
        set(value) {
            field = value
            _adapter.replace(value)
        }

    var listener: ActionSheetListener? = null

    private val _imageView: ImageView
    private val _textView: TextView
    private val _textView2: TextView
    private val _listView: RecyclerView
    private val _adapter = RecyclerViewAdapter<Any>()

    companion object {
        private const val TAG = "XHBActionSheet"
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.action_sheet, this)
        _imageView = findViewById(R.id.imageView)
        _textView = findViewById(R.id.textView)
        _textView2 = findViewById(R.id.textView2)
        _listView = findViewById(R.id.listView)

        _adapter.setItemBinding(ItemBinding())
        _adapter.setOnItemClickListener { i: Int, _: Any ->
            listener?.onAction(this, i)
        }
        _listView.adapter = _adapter
        _listView.addItemDecoration(DividerDecoration(LinearLayout.VERTICAL, 1, ContextCompat.getColor(context, R.color.blue_100)))
        _listView.layoutManager = LinearLayoutManager(context)

        val a = context.obtainStyledAttributes(attrs, R.styleable.XHBActionSheet, R.attr.actionSheetStyle, 0)
        title = a.getText(R.styleable.XHBActionSheet_title)
        subTitle = a.getText(R.styleable.XHBActionSheet_subTitle)
        icon = a.getResourceId(R.styleable.XHBActionSheet_icon, 0)
        val titlesId = a.getResourceId(R.styleable.XHBActionSheet_titles, 0)
        a.recycle()

        if (titlesId > 0) {
            titles = resources.getStringArray(titlesId).toList()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        _listView.removeItemDecorationAt(0)
        _listView.addItemDecoration(DividerDecoration(LinearLayout.VERTICAL, 1, ContextCompat.getColor(context, R.color.blue_100)))
    }

    /* private */

    class ItemBinding : BaseItemBinding<Any>() {

        override fun getItemViewType(item: Any?): Int {
            return R.layout.action_sheet_item
        }

        override fun bindView(view: View, item: Any?, position: Int) {
            view.findViewById<TextView>(R.id.title).text = item.toString()
        }

    }

}