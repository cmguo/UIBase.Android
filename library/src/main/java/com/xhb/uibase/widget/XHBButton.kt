package com.xhb.uibase.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.PorterDuff
import android.graphics.drawable.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.xhb.uibase.R
import com.xhb.uibase.resources.ShapeDrawables

class XHBButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatButton(context, attrs)
{
    enum class ButtonType(@StyleRes val resId: Int) {
        Primitive(R.style.XHBButton_Primitive),
        Secondary(R.style.XHBButton_Secondary),
        Tertiary(R.style.XHBButton_Tertiary),
        Danger(R.style.XHBButton_Danger),
        Text(R.style.XHBButton_Text)
    }

    enum class ButtonSize(@StyleRes val resId: Int) {
        Large(R.style.XHBButton_Large),
        Middle(R.style.XHBButton_Middle),
        Small(R.style.XHBButton_Small),
        Thin(R.style.XHBButton_Thin)
    }

    var buttonType: ButtonType = ButtonType.Primitive
        set(value) {
            if (field == value)
                return
            field = value
            if (_inited)
                syncType()
        }

    var buttonSize: ButtonSize = ButtonSize.Large
        set(value) {
            if (field == value)
                return
            field = value
            if (_inited)
                syncSize()
        }

    var buttonAppearance: Int = 0
        set(value) {
            field = value
            if (_inited)
                syncTypeSize()
        }

    var content: Int = 0
        set(value) {
            if (field == value)
                return
            field = value
            syncContent()
        }

    var icon: Int = 0
        set(value) {
            if (field == value)
                return
            field = value
            _icon = if (value > 0)
                ContextCompat.getDrawable(context, value)!!
            else
                null
            if (_inited)
                syncIcon()
        }

    var iconAtRight = false
        set(value) {
            if (field == value)
                return
            field = value
            if (_inited && !loading)
                syncCompoundDrawable()
        }

    var loadingDrawable: Drawable? = null
        set(value) {
            field = value
            if (loading) {
                syncIcon(true)
            }
        }

    var loadingText: CharSequence? = null
        set(value) {
            field = value
            if (loading)
                text = value
        }

    var loading: Boolean = false
        set(value) {
            if (field == value)
                return
            field = value
            syncCompoundDrawable()
            // swap text
            val t = if (loading) loadingText else _text
            setTextInner(t)
        }

    private var _inited = false
    private var _icon: Drawable? = null
    private var _text: CharSequence? = null

    data class TypeStyles(val textColor: ColorStateList?, val backgroundColor: ColorStateList?)
    data class SizeStyles(val height: Int, val radius: Float, val padding: Int,
                          val textSize: Float, val iconSize: Int, val iconPadding: Int)

    private lateinit var _typeStyles: TypeStyles
    private lateinit var _sizeStyles: SizeStyles

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.XHBButton,
                    R.attr.buttonStyle, 0)
            applyStyle(a)
            a.recycle()
        }
        syncTypeSize()
        syncIcon()
        _inited = true
    }

    override fun setText(text: CharSequence?, type: BufferType) {
        _text = text
        if (!loading)
            setTextInner(text, type)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        if (buttonAppearance != 0 || widthMode != MeasureSpec.EXACTLY) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }
        val width = MeasureSpec.getSize(widthMeasureSpec)
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.UNSPECIFIED), heightMeasureSpec)
        val width2 = measuredWidth
        if (width2 < width) {
            val d = (width - width2) / 2
            setPadding(paddingLeft + d, paddingTop, paddingRight + d, paddingBottom)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        super.setLayoutParams(params)
        // restore padding
        val padding = _sizeStyles.padding
        setPadding(padding, 0, padding, 0)
    }

    /* private */

    companion object {

        @SuppressLint("ResourceType")
        private fun typeStyles(context: Context, @StyleRes id: Int) : TypeStyles {
            val a = context.obtainStyledAttributes(id, R.styleable.XHBButton_Type)
            val styles = TypeStyles(
                a.getColorStateList(R.styleable.XHBButton_Type_textColor),
                a.getColorStateList(R.styleable.XHBButton_Type_backgroundColor)
            )
            a.recycle()
            return styles
        }

        private fun sizeStyles(context: Context, @StyleRes id: Int) : SizeStyles {
            val a = context.obtainStyledAttributes(id, R.styleable.XHBButton_Size)
            val styles = SizeStyles(
                a.getDimensionPixelSize(R.styleable.XHBButton_Size_height, 0),
                a.getDimension(R.styleable.XHBButton_Size_borderRadius, 0f),
                a.getDimensionPixelSize(R.styleable.XHBButton_Size_paddingX, 0),
                a.getDimension(R.styleable.XHBButton_Size_textSize, 0f),
                a.getDimensionPixelSize(R.styleable.XHBButton_Size_iconSize, 0),
                a.getDimensionPixelSize(R.styleable.XHBButton_Size_iconPadding, 0)
            )
            a.recycle()
            return styles
        }

        private fun backgroundDrawable(types: TypeStyles, sizes: SizeStyles) : Drawable? {
            // it's stateful, so can't shard
            if (types.backgroundColor == null)
                return null
            return createBackgroundDrawable(types, sizes)
        }

        private fun createBackgroundDrawable(types: TypeStyles, sizes: SizeStyles) : Drawable {
//            return DrawableBuilder().apply {
//                rectangle()
//                solidColorStateList(context.resources.getColorStateList(types.backgroundColor))
//                cornerRadius(context.resources.getDimensionPixelSize(sizes.radius))
//            }.build();
            val config = ShapeDrawables.RawConfig(GradientDrawable.RECTANGLE, sizes.radius, types.backgroundColor,
                    0, null, 0, 0)
            return ShapeDrawables.createDrawable(config)
        }
    }

    private fun applyStyle(a: TypedArray) {
        val type = a.getInt(R.styleable.XHBButton_buttonType, -1)
        if (type >= 0 && type != buttonType.ordinal)
            buttonType = ButtonType.values()[type]
        val size = a.getInt(R.styleable.XHBButton_buttonSize, -1)
        if (size >= 0 && size != buttonSize.ordinal)
            buttonSize = ButtonSize.values()[size]
        buttonAppearance = a.getResourceId(R.styleable.XHBButton_buttonAppearance, buttonAppearance)
        icon = a.getResourceId(R.styleable.XHBButton_icon, 0)
        content = a.getResourceId(R.styleable.XHBButton_content, 0)
        loadingDrawable = a.getDrawable(R.styleable.XHBButton_loadingDrawable)
        loadingText = a.getText(R.styleable.XHBButton_loadingText)
        iconAtRight = a.getBoolean(R.styleable.XHBButton_iconAtRight, iconAtRight)
    }

    private fun setTextInner(text: CharSequence?, type: BufferType = BufferType.NORMAL) {
        super.setText(text, type)
        if (_inited)
            syncIconPadding()
    }

    private fun syncTypeSize(type: Boolean = true, size: Boolean = true) {
        if (type)
            _typeStyles = typeStyles(context, if (buttonAppearance == 0) buttonType.resId else buttonAppearance)
        if (size)
            _sizeStyles = sizeStyles(context, if (buttonAppearance == 0) buttonSize.resId else buttonAppearance)
        background = backgroundDrawable(_typeStyles, _sizeStyles)
        if (type) {
            setTextColor(_typeStyles.textColor)
            if (_icon is VectorDrawable)
                _icon?.setTintList(textColors)
        }
        if (size) {
            height = _sizeStyles.height
            val padding = _sizeStyles.padding
            setPadding(padding, 0, padding, 0)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, _sizeStyles.textSize)
            val iconSize = _sizeStyles.iconSize
            _icon?.setBounds(0, 0, iconSize, iconSize)
            loadingDrawable?.setBounds(0, 0, iconSize, iconSize)
            if (_inited)
                syncCompoundDrawable()
            syncIconPadding()
        }
    }

    private fun syncType() {
        syncTypeSize(size = false)
    }

    private fun syncSize() {
        syncTypeSize(type = false)
    }

    private fun syncContent() {
        if (content == 0)
            return
        when (resources.getResourceTypeName(content)) {
            "drawable" -> { icon = content; text = null }
            "string" -> { icon = 0; setText(content) }
            "array" -> {
                icon = 0
                text = null
                val typedArray = resources.obtainTypedArray(content)
                for (i in 0 until typedArray.length()) {
                    val id = typedArray.getResourceId(i, 0)
                    when (resources.getResourceTypeName(id)) {
                        "drawable" -> icon = id
                        "string" -> setText(id)
                    }
                }
                typedArray.recycle()
            }
            "style" -> {
                icon = 0
                text = null
                val typedArray = context.obtainStyledAttributes(content, R.styleable.XHBButton)
                applyStyle(typedArray)
                typedArray.recycle()
            }
        }
    }

    private fun syncIcon(loading: Boolean = false) {
        val icon = if (loading) loadingDrawable else _icon
        if (icon != null) {
            if (icon is VectorDrawable) {
                icon.setTintMode(PorterDuff.Mode.SRC_IN)
                icon.setTintList(textColors)
            }
            val iconSize = _sizeStyles.iconSize
            icon.setBounds(0, 0, iconSize, iconSize)
        }
        if (loading == this.loading) {
            syncCompoundDrawable()
        }
    }

    private fun syncCompoundDrawable() {
        val o = if (loading) _icon else loadingDrawable
        val d = if (loading) loadingDrawable else _icon
        // How to center icon and text in a android button with width set to “fill parent”
        // https://stackoverflow.com/questions/3634191/how-to-center-icon-and-text-in-a-android-button-with-width-set-to-fill-parent
        val left = if (iconAtRight) null else d
        val right = if (iconAtRight) d else null
        if (buttonAppearance == 0) // TODO: extends iconAtRight to support more icon position mode
            setCompoundDrawablesRelative(left, null, right, null)
        else
            setCompoundDrawablesRelative(null, left, null, right)
        if (d is Animatable)
            d.start()
        if (o is Animatable)
            o.stop()
    }

    private fun syncIconPadding() {
        compoundDrawablePadding = if (text.count() == 0) 0 else _sizeStyles.iconPadding
    }

}
