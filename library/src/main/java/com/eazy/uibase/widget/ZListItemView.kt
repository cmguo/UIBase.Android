package com.eazy.uibase.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StyleRes
import com.eazy.uibase.R
import com.eazy.uibase.view.textAppearance

@SuppressLint("ClickableViewAccessibility")
class ZListItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null)
    : LinearLayout(context, attrs) {

    class Appearance(context: Context? = null, header: Boolean = false, @StyleRes resid: Int = 0) {
        var paddingX: Int = if (header) R.dimen.list_header_view_padding_x else R.dimen.list_item_view_padding_x
        var paddingY: Int = R.dimen.list_item_view_padding_y
        var subTextPadding: Int = R.dimen.list_item_view_sub_text_padding
        var textAppearance = R.style.TextAppearance_Z_Body_Middle
        var subTextAppearance = if (header)  R.style.TextAppearance_Z_Body_Secondary_ListItem else
            R.style.TextAppearance_Z_Body_Secondary
        var iconSize: Int = R.dimen.list_item_view_icon_size
        var iconPadding: Int = R.dimen.list_item_view_icon_padding
        var iconSize2: Int = R.dimen.list_item_view_icon_size2
        var iconPadding2: Int = R.dimen.list_item_view_icon_padding2

        var buttonAppearence = R.style.ZButton_Appearance_ListItem

        init {
            if (context != null) {
                val res = context.resources
                paddingX = res.getDimensionPixelSize(paddingX)
                paddingY = res.getDimensionPixelSize(paddingY)
                subTextPadding = res.getDimensionPixelSize(subTextPadding)
                iconSize = res.getDimensionPixelSize(iconSize)
                iconPadding = res.getDimensionPixelSize(iconPadding)
                iconSize2 = res.getDimensionPixelSize(iconSize2)
                iconPadding2 = res.getDimensionPixelSize(iconPadding2)
            }
            if (context != null && resid != 0) {
                val a = context.obtainStyledAttributes(resid, R.styleable.ZListItemAppearance)
                paddingX = a.getDimensionPixelSize(R.styleable.ZListItemAppearance_paddingX, paddingX)
                paddingY = a.getDimensionPixelSize(R.styleable.ZListItemAppearance_paddingY, paddingY)
                subTextPadding = a.getDimensionPixelSize(R.styleable.ZListItemAppearance_subTextPadding, subTextPadding)
                textAppearance = a.getResourceId(R.styleable.ZListItemAppearance_textAppearance, textAppearance)
                subTextAppearance = a.getResourceId(R.styleable.ZListItemAppearance_subTextAppearance, subTextAppearance)
                iconSize = a.getDimensionPixelSize(R.styleable.ZListItemAppearance_iconSize, iconSize)
                iconPadding = a.getDimensionPixelSize(R.styleable.ZListItemAppearance_iconPadding, iconPadding)
                iconSize2 = a.getDimensionPixelSize(R.styleable.ZListItemAppearance_iconSize2, iconSize2)
                iconPadding2 = a.getDimensionPixelSize(R.styleable.ZListItemAppearance_iconPadding2, iconPadding2)
                buttonAppearence = a.getResourceId(R.styleable.ZListItemAppearance_buttonAppearence, buttonAppearence)
                a.recycle()
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
        val badge: Any?
    }

    interface GroupData : Data {
        val items: Iterable<Data>
    }

    var appearance: Appearance = DefaultStyle
        set(value) {
            field = value
            _titleView.textAppearance = appearance.textAppearance
            _subTitleView.textAppearance = appearance.subTextAppearance
        }

    private val _iconView: ImageView
    private val _titleView: TextView
    private val _subTitleView: TextView
    internal var _contentType: ContentType? = null
    internal var _contentView: View? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.list_item_view, this)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        _iconView = findViewById(R.id.icon)
        _titleView = findViewById(R.id.title)
        _subTitleView = findViewById(R.id.subTitle)
    }

    fun setData(data: Data, listView: ZListWidgetCache) {
        val lpST = _subTitleView.layoutParams as LayoutParams
        lpST.topMargin = appearance.subTextPadding
        _subTitleView.layoutParams = lpST
        val lpIcon = _iconView.layoutParams as LayoutParams
        if (data.subTitle == null) {
            lpIcon.width  = appearance.iconSize
            lpIcon.height = appearance.iconSize
            lpIcon.rightMargin = appearance.iconPadding
            _subTitleView.visibility = View.GONE
        } else {
            lpIcon.width  = appearance.iconSize2
            lpIcon.height = appearance.iconSize2
            lpIcon.rightMargin = appearance.iconPadding2
            _subTitleView.visibility = View.VISIBLE
        }
        _iconView.visibility = if (data.icon == null) View.GONE else View.VISIBLE
        _iconView.layoutParams = lpIcon
        _iconView.setImageDrawable(data.icon as? Drawable)
        setPadding(appearance.paddingX, appearance.paddingY, appearance.paddingX, appearance.paddingY)
        _titleView.text = data.title
        _subTitleView.text = data.subTitle
        if (_contentType != null && _contentType != data.contentType) {
            val contentType = _contentType
            val contentView = _contentView
            _contentType = null
            _contentView = null
            removeView(contentView)
            listView.enqueueContentView(contentType!!, contentView!!)
        }
        if (data.contentType != null) {
            if (_contentType != data.contentType) {
                _contentType = data.contentType
                _contentView = listView.dequeContentView(data.contentType!!, this)
                val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                lp.leftMargin = lpIcon.rightMargin
                lp.gravity = Gravity.CENTER_VERTICAL
                addView(_contentView, lp)
            }
            listView.bindContent(_contentType!!, _contentView!!, appearance, data.content)
        }
        if (data.badge != null) {
            val badge = ZBadgeView(context)
            badge.text = data.badge as? String ?: ""
            badge.bindTarget(_contentView)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }

    companion object {
        private val DefaultStyle = Appearance()
    }
}