package com.eazy.uibase.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.TypedArray
import android.graphics.Rect
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import androidx.annotation.AnyRes
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatButton
import com.eazy.uibase.R
import com.eazy.uibase.resources.Drawables
import com.eazy.uibase.resources.RoundDrawable

open class ZButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.buttonStyle
) : AppCompatButton(context, attrs)
{
    enum class ButtonType(@StyleRes val resId: Int) {
        Primitive(R.style.ZButton_Appearance_Primitive),
        Secondary(R.style.ZButton_Appearance_Secondary),
        Tertiary(R.style.ZButton_Appearance_Tertiary),
        Danger(R.style.ZButton_Appearance_Danger),
        Text(R.style.ZButton_Appearance_Text),
        TextTransparent(R.style.ZButton_Appearance_Text_Transparent),
        TextTransparentLight(R.style.ZButton_Appearance_Text_Transparent_Light)
    }

    enum class ButtonSize(@StyleRes val resId: Int) {
        Large(R.style.ZButton_Appearance_Large),
        Middle(R.style.ZButton_Appearance_Middle),
        Small(R.style.ZButton_Appearance_Small),
        Thin(R.style.ZButton_Appearance_Thin)
    }

    enum class IconPosition {
        Left,
        Top,
        Right,
        Bottom
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

    @StyleRes
    var buttonAppearance: Int = 0
        set(value) {
            if (field == value)
                return
            field = value
            if (_inited)
                syncTypeSize()
        }

    @AnyRes
    var content: Int = 0
        set(value) {
            if (field == value)
                return
            field = value
            syncContent()
        }

    @DrawableRes
    open var icon: Int = 0
        set(value) {
            if (field == value)
                return
            field = value
            _icon = if (value > 0)
                Drawables.getDrawable(context, value)!!
            else
                null
            if (_inited)
                syncIcon()
        }

    var iconPosition = IconPosition.Left
        set(value) {
            if (field == value)
                return
            field = value
            if (_inited && !loading)
                syncCompoundDrawable()
        }

    var iconDrawable: Drawable?
        get() = _icon
        set(value) {
            _icon = value
            syncIcon()
        }

    @DrawableRes
    var loadingIcon: Int = 0
        set(value) {
            if (field == value)
                return
            field = value
            _loadingIcon = if (value > 0)
                Drawables.getDrawable(context, value)!!
            else
                null
            if (_inited)
                syncIcon(true)
        }

    var loadingIconDrawable: Drawable?
        get() = _loadingIcon
        set(value) {
            _loadingIcon = value
            if (_inited)
                syncIcon(true)
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
            if (_loadingIcon == null && loadingText == null)
                return
            syncCompoundDrawable()
            // swap text
            val t = if (loading) loadingText else _text
            setTextInner(t)
        }

    private var _inited = false
    private var _icon: Drawable? = null
    private var _loadingIcon: Drawable? = null
    private var _text: CharSequence? = null

    data class TypeStyles(val textColor: ColorStateList?, val backgroundColor: ColorStateList?, val iconPosition: IconPosition)
    data class SizeStyles(val height: Int, val cornerRadius: Float, val padding: Int,
                          val textSize: Float, val iconSize: Int, val iconPadding: Int)

    private lateinit var _typeStyles: TypeStyles
    private lateinit var _sizeStyles: SizeStyles

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ZButton, defStyleAttr, 0)
        applyStyle(a)
        a.recycle()
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
        //Log.d(TAG, "onMeasure: " + MeasureSpec.toString(widthMeasureSpec) + " " + MeasureSpec.toString(heightMeasureSpec))
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        if (buttonAppearance != 0 || widthMode != MeasureSpec.EXACTLY) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            //Log.d(TAG, "onMeasure: $measuredWidth $measuredHeight")
            return
        }
        val width = MeasureSpec.getSize(widthMeasureSpec)
        super.onMeasure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), heightMeasureSpec)
        val width2 = measuredWidth
        if (width2 != width) {
            // prefect smaller padding to make enough room for content
            val d = if (width > width2) (width - width2) / 2 else (width - width2 - 1) / 2
            setPadding(paddingLeft + d, paddingTop, paddingRight + d, paddingBottom)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //Log.d(TAG, "onMeasure: $measuredWidth $measuredHeight")
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        super.setLayoutParams(params)
        // restore padding
        val padding = _sizeStyles.padding
        setPadding(padding, 0, padding, 0)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (icon != 0 && _icon !is VectorDrawable) {
            _icon = Drawables.getDrawable(context, icon)
            syncIcon()
        }
        if (loadingIcon != 0 && _loadingIcon !is VectorDrawable) {
            _loadingIcon = Drawables.getDrawable(context, loadingIcon)
            syncIcon(true)
        }
        syncType()
    }

    /* private */

    companion object {

        private const val TAG = "ZButton"

        @SuppressLint("ResourceType")
        private fun typeStyles(context: Context, @StyleRes id: Int) : TypeStyles {
            val a = context.obtainStyledAttributes(id, R.styleable.ZButton_Type)
            val type = a.getInt(R.styleable.ZButton_Type_buttonType, -1)
            val styles2 = if (type >= 0) typeStyles(context, ButtonType.values()[type].resId) else null
            val styles = TypeStyles(
                a.getColorStateList(R.styleable.ZButton_Type_textColor) ?: styles2?.textColor,
                a.getColorStateList(R.styleable.ZButton_Type_backgroundColor) ?: styles2?.backgroundColor,
                IconPosition.values()[a.getInt(R.styleable.ZButton_Type_iconPosition, styles2?.iconPosition?.ordinal ?: 0)]
            )
            a.recycle()
            return styles
        }

        private fun sizeStyles(context: Context, @StyleRes id: Int) : SizeStyles {
            val a = context.obtainStyledAttributes(id, R.styleable.ZButton_Size)
            val size = a.getInt(R.styleable.ZButton_Size_buttonSize, -1)
            val size2 = if (size >= 0) sizeStyles(context, ButtonSize.values()[size].resId) else null
            val styles = SizeStyles(
                a.getDimensionPixelSize(R.styleable.ZButton_Size_height, size2?.height ?: 0),
                a.getDimension(R.styleable.ZButton_Size_cornerRadius, size2?.cornerRadius ?: 0f),
                a.getDimensionPixelSize(R.styleable.ZButton_Size_paddingX, size2?.padding ?: 0),
                a.getDimension(R.styleable.ZButton_Size_textSize, size2?.textSize ?: 0f),
                a.getDimensionPixelSize(R.styleable.ZButton_Size_iconSize, size2?.iconSize ?: 0),
                a.getDimensionPixelSize(R.styleable.ZButton_Size_iconPadding, size2?.iconPadding ?: 0)
            )
            a.recycle()
            return styles
        }

        private fun backgroundDrawable(types: TypeStyles, sizes: SizeStyles) : Drawable? {
            // it's stateful, so can't shard
            if (types.backgroundColor == null)
                return null
            return RoundDrawable(types.backgroundColor, sizes.cornerRadius)
        }
    }

    private fun applyStyle(a: TypedArray) {
        val type = a.getInt(R.styleable.ZButton_buttonType, -1)
        if (type >= 0)
            buttonType = ButtonType.values()[type]
        val size = a.getInt(R.styleable.ZButton_buttonSize, -1)
        if (size >= 0)
            buttonSize = ButtonSize.values()[size]
        content = a.getResourceId(R.styleable.ZButton_content, 0)
        buttonAppearance = a.getResourceId(R.styleable.ZButton_buttonAppearance, buttonAppearance)
        icon = a.getResourceId(R.styleable.ZButton_icon, 0)
        loadingIcon = a.getResourceId(R.styleable.ZButton_loadingIcon, 0)
        loadingText = a.getText(R.styleable.ZButton_loadingText)
        iconPosition = IconPosition.values()[a.getInt(R.styleable.ZButton_iconPosition, iconPosition.ordinal)]
    }

    private fun setTextInner(text: CharSequence?, type: BufferType = BufferType.NORMAL) {
        super.setText(text, type)
        if (_inited)
            syncIconPadding()
    }

    private fun syncTypeSize(type: Boolean = true, size: Boolean = true) {
        if (type) {
            _typeStyles = typeStyles(context, if (buttonAppearance == 0) buttonType.resId else buttonAppearance)
            iconPosition = _typeStyles.iconPosition
        }
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
            _loadingIcon?.setBounds(0, 0, iconSize, iconSize)
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
            "drawable" -> {
                icon = content; text = null
            }
            "string" -> {
                icon = 0; setText(content)
            }
            "array" -> {
                icon = 0
                text = null
                val a = resources.obtainTypedArray(content)
                val v = TypedValue()
                for (i in 0 until a.length()) {
                    if (a.getValue(i, v)) {
                        if (v.resourceId != 0) {
                            when (resources.getResourceTypeName(v.resourceId)) {
                                "drawable" -> icon = v.resourceId
                                "string" -> setText(v.resourceId)
                            }
                        } else if (v.string.isNotEmpty()) {
                            text = v.string
                        }
                    }

                }
                a.recycle()
            }
            "style" -> {
                icon = 0
                text = null
                val typedArray = context.obtainStyledAttributes(content, R.styleable.ZButton)
                applyStyle(typedArray)
                typedArray.recycle()
            }
        }
    }

     private fun syncIcon(loading: Boolean = false) {
        val icon = if (loading) _loadingIcon else _icon
        if (icon != null) {
            icon.mutate()
//            if (icon is VectorDrawable || Drawables.isPureColor(icon)) {
//                icon.setTintList(textColors)
//            }
            val iconSize = _sizeStyles.iconSize
            icon.setBounds(0, 0, iconSize, iconSize)
        }
        if (loading == this.loading) {
            syncCompoundDrawable()
        }
    }

    private var _lastIcon: Drawable? = null

    private fun syncCompoundDrawable() {
        val o = _lastIcon
        val d = if (loading) _loadingIcon else _icon
        if (o == d)
            return
        // How to center icon and text in a android button with width set to “fill parent”
        // https://stackoverflow.com/questions/3634191/how-to-center-icon-and-text-in-a-android-button-with-width-set-to-fill-parent
        val left = if (iconPosition == IconPosition.Left) d else null
        val top = if (iconPosition == IconPosition.Top) d else null
        val right = if (iconPosition == IconPosition.Right) d else null
        val bottom = if (iconPosition == IconPosition.Bottom) d else null
        val compoundRect = Rect()
        d?.copyBounds(compoundRect)
        // setCompoundDrawablesRelative has measure problem on TextView
        //  --Relative calc sizes of start/end, but measure use left/right sizes
        setCompoundDrawables(left, top, right, bottom)
        if (d is Animatable)
            d.start()
        if (o is Animatable)
            o.stop()
        _lastIcon = d
    }

    private fun syncIconPadding() {
        compoundDrawablePadding = if (text.count() == 0) 0 else _sizeStyles.iconPadding
    }

}
