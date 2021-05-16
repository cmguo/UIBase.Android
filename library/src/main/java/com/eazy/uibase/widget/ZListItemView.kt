package com.eazy.uibase.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import com.eazy.uibase.R

@SuppressLint("ClickableViewAccessibility")
class ZListItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null)
    : LinearLayout(context, attrs) {

    class Style(header: Boolean = false, a: TypedArray? = null) {
        var paddingX: Int = if (header) 0 else 16
        var paddingY: Int = 10
        var subTextPadding: Int = 16
        var textAppearance = R.style.TextAppearance_Z_Body_Middle
        var subTextAppearance = if (header)  R.style.TextAppearance_Z_Secondary_ListItem else
            R.style.TextAppearance_Z_Secondary
        var iconSize: Int = 18
        var iconPadding: Int = 4
        var iconSize2: Int = 40
        var iconPadding2: Int = 8

        var buttonAppearence = R.style.ZButton_ListItem

        init {
            if (a != null) {
                paddingX = a.getDimensionPixelSize(R.styleable.ZListItemView_paddingX, paddingX)
                paddingY = a.getDimensionPixelSize(R.styleable.ZListItemView_paddingY, paddingY)
                subTextPadding = a.getDimensionPixelSize(R.styleable.ZListItemView_subTextPadding, subTextPadding)
                textAppearance = a.getResourceId(R.styleable.ZListItemView_textAppearance, textAppearance)
                subTextAppearance = a.getResourceId(R.styleable.ZListItemView_subTextAppearance, subTextAppearance)
                iconSize = a.getDimensionPixelSize(R.styleable.ZListItemView_iconSize, iconSize)
                iconPadding = a.getDimensionPixelSize(R.styleable.ZListItemView_iconPadding, iconPadding)
                iconSize2 = a.getDimensionPixelSize(R.styleable.ZListItemView_iconSize2, iconSize2)
                iconPadding2 = a.getDimensionPixelSize(R.styleable.ZListItemView_iconPadding2, iconPadding2)
                buttonAppearence = a.getResourceId(R.styleable.ZListItemView_buttonAppearence, buttonAppearence)
            }
        }
    }

    enum class ContentType {
        Button,
        TextField,
        CheckBox,
        RadioButton,
        SwitchButton
    }

    interface Data {
        val title: String
        val subTitle: String?
        val icon: Any?
        val contentType: ContentType?
        val content: Any?
        var badge: Any?
    }

    interface GroupData : Data {
        val items: Iterable<Data>
    }

    var style: Style = DefaultStyle
        set(value) {
            field = value
            TextViewCompat.setTextAppearance(_titleView, style.textAppearance)
            TextViewCompat.setTextAppearance(_subTitleView, style.subTextAppearance)
        }

    private val _iconView: ImageView = findViewById(R.id.icon)
    private val _titleView: TextView = findViewById(R.id.title)
    private val _subTitleView: TextView = findViewById(R.id.subTitle)
    private var _contentType: ContentType? = null
    private var _contentView: View? = null

    fun setData(data: Data, listView: ZListView) {
        val lpST = _subTitleView.layoutParams as LayoutParams
        lpST.topMargin = style.subTextPadding
        _subTitleView.layoutParams = lpST
        val lpIcon = _iconView.layoutParams as LayoutParams
        if (data.subTitle == null) {
            lpIcon.width  = style.iconSize
            lpIcon.height = style.iconSize
            lpIcon.rightMargin = style.iconPadding
            _subTitleView.visibility = View.GONE
        } else {
            lpIcon.width  = style.iconSize2
            lpIcon.height = style.iconSize2
            lpIcon.rightMargin = style.iconPadding2
            _subTitleView.visibility = View.VISIBLE
        }
        _iconView.visibility = if (data.icon == null) View.GONE else View.VISIBLE
        _iconView.layoutParams = lpIcon
        setPadding(style.paddingX, style.paddingY, style.paddingX, style.paddingY)
        _contentView = if (data.contentType == null) null else listView.dequeContentView(data.contentType!!)
        _contentType = data.contentType
        if (_contentView != null) {
            val lp = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            lp.leftMargin = lpIcon.rightMargin
            addView(_contentView, lp)
            listView.bindContent(_contentType!!, _contentView!!, data.content)
        }
        if (data.badge != null) {
            val badge = ZBadgeView(context)
            badge.text = data.badge as? String ?: ""
            badge.bindTarget(_contentView)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (_contentType != null) {
            removeView(_contentView)
            (parent as ZListView).enqueueContentView(_contentType!!, _contentView!!)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }

    companion object {
        private val DefaultStyle = Style()
    }
}