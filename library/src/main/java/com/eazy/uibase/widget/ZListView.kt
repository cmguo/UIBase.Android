package com.eazy.uibase.widget

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eazy.uibase.R
import com.eazy.uibase.view.list.DividerDecoration
import com.eazy.uibase.view.parentOfType

@SuppressLint("CustomViewStyleable")
class ZListView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.listViewStyle)
    : RecyclerView(context, attrs, defStyleAttr) {

    interface OnItemValueChangeListener {
        fun onItemValueChanged(listView: ZListView, item: IntArray, value: Any?)
    }

    var data: Iterable<ZListItemView.Data> = ArrayList()
        set(value) {
            field = value
            syncData()
        }

    var headerStyle = ZListItemView.Appearance(context, true)

    var itemStyle = ZListItemView.Appearance(context)

    var listener: OnItemValueChangeListener? = null

    private val _adapter = ListAdapter(this)

    private var _count = 0
    private var _groups = intArrayOf()

    companion object {
        private const val TAG = "ZListView"
    }

    init {
        this.adapter = _adapter
        this.addItemDecoration(DividerDecoration(LinearLayout.VERTICAL, 1f, ContextCompat.getColor(context, R.color.blue_100)))
        this.layoutManager = LinearLayoutManager(context)

        val a = context.obtainStyledAttributes(attrs, R.styleable.ZListView, defStyleAttr, 0)
        val headerStyle = a.getResourceId(R.styleable.ZListView_headerAppearance, 0)
        val itemStyle = a.getResourceId(R.styleable.ZListView_itemAppearance, 0)
        a.recycle()

        if (headerStyle > 0) {
            this.headerStyle = ZListItemView.Appearance(context, true, headerStyle)
        }
        if (itemStyle > 0) {
            this.itemStyle = ZListItemView.Appearance(context, false, itemStyle)
        }
    }

    fun findViewHolderForAdapterPosition(position: IntArray) : ViewHolder? {
        val pos = _groups[position[0]] + position[1]
        return super.findViewHolderForAdapterPosition(pos)
    }

    /* internal */

    private val _contentViewCache : MutableMap<ZListItemView.ContentType, MutableList<View>> = mutableMapOf()
    private val _radioButtons = mutableListOf<ZRadioButton>()

    internal fun dequeContentView(contentType: ZListItemView.ContentType): View {
        val cache = _contentViewCache[contentType]
        val view = cache?.firstOrNull()
        if (view != null) {
            cache.removeAt(0)
            return view
        }
        return when (contentType) {
            ZListItemView.ContentType.Button -> {
                val button = ZButton(context)
                button.buttonAppearance = itemStyle.buttonAppearence
                button.setOnClickListener { listItemChanged(it, null) }
                button
            }
            ZListItemView.ContentType.CheckBox -> {
                val checkBox = ZCheckBox(context)
                checkBox.setOnCheckedStateChangeListener(object : ZCheckBox.OnCheckedStateChangeListener {
                    override fun onCheckedStateChanged(checkBox: ZCheckBox, state: ZCheckBox.CheckedState) {
                        listItemChanged(checkBox, state)
                    }
                })
                checkBox
            }
            ZListItemView.ContentType.RadioButton -> {
                val radioButton = ZRadioButton(context)
                radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
                    listItemChanged(buttonView, isChecked)
                    if (isChecked && _radioButtons.contains(buttonView)) {
                        for (rb in _radioButtons) {
                            if (rb != buttonView)
                                rb.isChecked = false
                        }
                    }
                }
                radioButton
            }
            ZListItemView.ContentType.SwitchButton -> {
                val switchButton = ZSwitchButton(context)
                switchButton.setOnCheckedChangeListener { buttonView, isChecked ->
                    listItemChanged(buttonView, isChecked)
                }
                switchButton
            }
            ZListItemView.ContentType.TextField -> {
                val textField = EditText(context)
                textField.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        listItemChanged(textField, s)
                    }
                })
                textField
            }
        }
    }

    internal fun bindContent(contentType: ZListItemView.ContentType, view: View, content: Any?) {
        when (contentType) {
            ZListItemView.ContentType.Button -> {
                if (view is ZButton)
                    view.content = content as? Int ?: 0
            }
            ZListItemView.ContentType.CheckBox -> {
                if (view is ZCheckBox)
                    view.checkedState = content as? ZCheckBox.CheckedState
                        ?: ZCheckBox.CheckedState.NotChecked
            }
            ZListItemView.ContentType.RadioButton -> {
                if (view is ZRadioButton) {
                    view.isChecked = content as? Boolean ?: false
                    if (view.isChecked) {
                        for (rb in _radioButtons)
                            rb.isChecked = false
                    }
                    _radioButtons.add(view)
                }
            }
            ZListItemView.ContentType.SwitchButton -> {
                if (view is ZSwitchButton)
                    view.isChecked = content as? Boolean ?: false
            }
            ZListItemView.ContentType.TextField -> {
                if (view is EditText)
                    view.setText(content as? CharSequence)
            }
        }
    }

    internal fun enqueueContentView(contentType: ZListItemView.ContentType, view: View) {
        if (contentType == ZListItemView.ContentType.RadioButton) {
            _radioButtons.remove(view as ZRadioButton)
        }
        var cache = _contentViewCache[contentType]
        if (cache == null) {
            cache = mutableListOf(view)
            _contentViewCache[contentType] = cache
        } else {
            cache.add(view)
        }
    }

    /* private */

    private fun syncData() {
        var count = 0
        val groups = mutableListOf<Int>()
        for (i in data) {
            if (i is ZListItemView.GroupData) {
                if (groups.isEmpty()) {
                    groups.addAll(0 until count)
                }
                groups.add(count)
                count += i.items.count()
            } else if (!groups.isEmpty()) {
                groups.add(count)
            }
            count++
        }
        if (groups.isEmpty())
            groups.add(0)
        _count = count
        _groups = groups.toIntArray()
        _adapter.notifyDataSetChanged()
    }

    private fun listItemChanged(view: View, value: Any?) {
        val itemView = view.parentOfType(ZListItemView::class.java) ?: return
        val position = getChildAdapterPosition(itemView)
        if (position < 0) return
        val group = _groups.indexOfLast { it <= position }
        listener?.onItemValueChanged(this, intArrayOf(group, position - _groups[group]), value)
    }

    private class ListHolder(view: View) : RecyclerView.ViewHolder(view)

    private class ListAdapter(private val outer: ZListView) : RecyclerView.Adapter<ListHolder>(), OnClickListener, ZCheckBox.OnCheckedStateChangeListener {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
            val view = ZListItemView(parent.context)
            view.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            if (viewType == 0) {
                view.appearance = outer.itemStyle
            } else {
                view.appearance = outer.headerStyle
            }
            view.setOnClickListener(this)
            view.findViewById<ZCheckBox>(R.id.checkBox)!!.setOnCheckedStateChangeListener(this)
            return ListHolder(view)
        }

        override fun getItemCount(): Int {
            return outer._count
        }

        fun getItem(position: Int) : ZListItemView.Data {
            val group = outer._groups.indexOfLast { it <= position }
            var data = outer.data.elementAt(group)
            val subpos = position - outer._groups[group]
            if (data is ZListItemView.GroupData && subpos > 0) {
                data = data.items.elementAt(subpos - 1)
            } else if (subpos > 0) {
                data = outer.data.elementAt(subpos)
            }
            return data
        }

        override fun getItemViewType(position: Int): Int {
            val data = getItem(position)
            return if (data is ZListItemView.GroupData) 1 else 0
        }

        override fun onBindViewHolder(holder: ListHolder, position: Int) {
            val data = getItem(position)
            (holder.itemView as ZListItemView).setData(data, outer)
        }

        override fun onClick(view: View) {
            if (view is ZListItemView && view._contentType != null) {
                when (view._contentType) {
                    ZListItemView.ContentType.Button -> outer.listItemChanged(view, null)
                    ZListItemView.ContentType.CheckBox -> (view._contentView as? ZCheckBox)?.toggle()
                    ZListItemView.ContentType.RadioButton -> (view._contentView as? ZRadioButton)?.toggle()
                    ZListItemView.ContentType.SwitchButton -> (view._contentView as? ZSwitchButton)?.toggle()
                    else -> {}
                }
            }
        }

        override fun onCheckedStateChanged(checkBox: ZCheckBox, state: ZCheckBox.CheckedState) {
        }
    }

}