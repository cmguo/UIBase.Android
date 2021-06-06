package com.eazy.uibase.widget

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.eazy.uibase.view.parentOfType

class ZListWidgetCache {

    /* internal */

    private val _contentViewCache : MutableMap<ZListItemView.ContentType, MutableList<View>> = mutableMapOf()
    private val _radioButtons = mutableListOf<ZRadioButton>()

    internal fun dequeContentView(contentType: ZListItemView.ContentType, parent: ViewGroup): View {
        val cache = _contentViewCache[contentType]
        val view = cache?.firstOrNull()
        if (view != null) {
            cache.removeAt(0)
            return view
        }
        val context = parent.context
        return when (contentType) {
            ZListItemView.ContentType.Button -> {
                val button = ZButton(context)
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

    internal fun bindContent(contentType: ZListItemView.ContentType, view: View, appearance: ZListItemView.Appearance, content: Any?) {
        when (contentType) {
            ZListItemView.ContentType.Button -> {
                if (view is ZButton) {
                    view.buttonAppearance = appearance.buttonAppearence
                    view.content = content as? Int ?: 0
                }
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

    companion object {

        internal fun onItemClicked(listView: ZListView, view: View) {
            if (view is ZListItemView) {
                when (view._contentType) {
                    ZListItemView.ContentType.Button -> listView.listItemChanged(view, null)
                    ZListItemView.ContentType.CheckBox -> (view._contentView as? ZCheckBox)?.toggle()
                    ZListItemView.ContentType.RadioButton -> (view._contentView as? ZRadioButton)?.toggle()
                    ZListItemView.ContentType.SwitchButton -> (view._contentView as? ZSwitchButton)?.toggle()
                    else -> listView.listItemChanged(view, null)
                }
            }
        }

    }

    /* private */

    private fun listItemChanged(view: View, value: Any?) {
        val itemView = view.parentOfType(ZListItemView::class.java) ?: return
        val listView = itemView.parentOfType(ZListView::class.java) ?: return
        listView.listItemChanged(itemView, value)
    }

}