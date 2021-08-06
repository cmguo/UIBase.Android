package com.eazy.uibase.widget

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eazy.uibase.R
import com.eazy.uibase.resources.Drawables
import com.eazy.uibase.view.list.ItemDecorations

class ZActionSheet @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.actionSheetStyle)
    : FrameLayout(context, attrs, defStyleAttr) {

    interface ActionSheetListener {
        fun onAction(sheet: ZActionSheet, index: Int)
    }

    @DrawableRes
    var icon: Int = 0
        set(value) {
            if (field == value) return
            field = value
            if (value == 0) {
                _imageView.visibility = View.GONE
            } else {
                _imageView.setImageDrawable(Drawables.getDrawable(context, value))
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

    var buttons: Iterable<Any?> = ArrayList()

    var states: Iterable<Any?>? = null

    var listener: ActionSheetListener? = null

    private val _imageView: ImageView
    private val _textView: TextView
    private val _textView2: TextView
    private val _listView: RecyclerView
    private val _adapter = ActionAdapter(this)

    companion object {
        private const val TAG = "ZActionSheet"
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.action_sheet, this)
        _imageView = findViewById(R.id.imageView)
        _textView = findViewById(R.id.textView)
        _textView2 = findViewById(R.id.textView2)
        _listView = findViewById(R.id.listView)

        _listView.adapter = _adapter
        _listView.addItemDecoration(ItemDecorations.divider(1f,
            ContextCompat.getColor(context, R.color.blue_100)).build(_listView))
        _listView.layoutManager = LinearLayoutManager(context)

        val a = context.obtainStyledAttributes(attrs, R.styleable.ZActionSheet, defStyleAttr, 0)
        title = a.getText(R.styleable.ZActionSheet_title)
        subTitle = a.getText(R.styleable.ZActionSheet_subTitle)
        icon = a.getResourceId(R.styleable.ZActionSheet_icon, 0)
        val buttonIds = a.getResourceId(R.styleable.ZActionSheet_buttons, 0)
        a.recycle()

        if (buttonIds > 0) {
            val aa = resources.obtainTypedArray(buttonIds)
            val v = TypedValue()
            val ids = Array<Any?>(aa.length()) { null }
            for (i in 0 until aa.length()) {
                if (aa.getValue(i, v)) {
                    if (v.resourceId != 0)
                        ids[i] = v.resourceId
                    else if (v.string.isNotEmpty())
                        ids[i] = v.string
                }
            }
            aa.recycle()
            buttons = listOf(ids)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        _listView.removeItemDecorationAt(0)
        _listView.addItemDecoration(ItemDecorations.divider(1f,
            ContextCompat.getColor(context, R.color.blue_100)).build(_listView))
    }

    /* private */

    private class ActionHolder(private val view: View)
        : RecyclerView.ViewHolder(view) {

        private val button = view.findViewById<ZButton>(R.id.button)!!

        fun bind(content: Any?, state: Any?) {
            val states = if (state is Int) intArrayOf(state) else state as? IntArray
            val enabled = !(states?.contains(-android.R.attr.state_enabled) ?: false)
            val selected = states?.contains(android.R.attr.state_selected) ?: false
            view.isEnabled = enabled
            button.isEnabled = enabled
            view.isSelected = selected
            button.isSelected = selected
            if (content is String) {
                button.text = content
            } else if (content is Int) {
                button.content = content
            }
        }
    }

    private class ActionAdapter(private val outer: ZActionSheet) : RecyclerView.Adapter<ActionHolder>(), OnClickListener {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.action_item, parent, false)
            view.setOnClickListener(this)
            return ActionHolder(view)
        }

        override fun getItemCount(): Int {
            return outer.buttons.count()
        }

        override fun onBindViewHolder(holder: ActionHolder, position: Int) {
            holder.bind(outer.buttons.elementAt(position), outer.states?.elementAtOrNull(position))
        }

        override fun onClick(view: View) {
            outer.listener?.onAction(outer, outer._listView.getChildAdapterPosition(view))
        }
    }


}