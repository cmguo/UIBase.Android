package com.eazy.uibase.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.TypedArray
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
import androidx.core.content.ContextCompat
import com.eazy.uibase.R
import com.eazy.uibase.resources.Drawables
import com.eazy.uibase.resources.GradientColorList
import com.eazy.uibase.resources.RoundDrawable
import com.eazy.uibase.resources.toGradient

open class ZButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.buttonStyle
) : AppCompatButton(context, attrs)
{
    enum class ButtonType(@StyleRes val resId: Int) {
        Primitive(R.style.ZButton_Appearance_Primitive),
        Secondary(R.style.ZButton_Appearance_Secondary),
        Tertiary(R.style.ZButton_Appearance_Tertiary),
        Danger(R.style.ZButton_Appearance_Danger),
        TextLink(R.style.ZButton_Appearance_TextLink)
    }

    enum class ButtonSize(@StyleRes val resId: Int) {
        Large(R.style.ZButton_Appearance_Large),
        Middle(R.style.ZButton_Appearance_Middle),
        Small(R.style.ZButton_Appearance_Small),
        Thin(R.style.ZButton_Appearance_Thin) // Only used by Text type button
    }

    enum class IconPosition {
        Left,
        Top,
        Right,
        Bottom
    }

    // store value from style or set manually, not affect by value in ButtonAppearance
    var buttonType: ButtonType
        get() = _appearance.buttonType()
        set(value) {
            syncAppearance(_appearance.setButtonType(context, value))
        }

    // store value from style or set manually, not affect by value in ButtonAppearance
    var buttonSize: ButtonSize
        get() = _appearance.buttonSize()
        set(value) {
            syncAppearance(_appearance.setButtonSize(context, value))
        }

    var buttonAppearance: Int
        get() = _appearance.buttonApperanceId()
        set(value) {
            syncAppearance(_appearance.setButtonApperanceId(context, value))
        }

    var backgroundColor: ColorStateList?
        get() = _appearance.backgroundColor
        set(value) {
            _appearance.backgroundColor = value
            if (_inited)
                syncAppearanceV(R.styleable.ZButton_Appearance_backgroundColor)
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
                Drawables.getDrawable(context, value)
            else
                null
            if (_inited)
                syncIcon()
        }

    var iconPosition : IconPosition
        get() = _appearance.iconPosition
        set(value) {
            if (_appearance.iconPosition == value)
                return
            _appearance.iconPosition = value
            if (_inited && !loading)
                syncCompoundDrawable(true)
        }

    var iconColor : ColorStateList?
        get() = _appearance.iconColor ?: _appearance.textColor
        set(value) {
            _appearance.iconColor = value
            if (_inited)
                syncAppearanceV(R.styleable.ZButton_Appearance_iconColor)
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

    private val _appearance = ZButtonAppearance()

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ZButton, defStyleAttr, 0)
        readStyle(a)
        a.recycle()
        syncAppearance()
        syncIcon()
        _inited = true
    }

    override fun setText(text: CharSequence?, type: BufferType) {
        _text = text
        if (!loading)
            setTextInner(text, type)
    }

    override fun setTextColor(color: Int) {
        if (!_inited) return
        _appearance.textColor = ColorStateList.valueOf(color)
        syncAppearanceV(R.styleable.ZButton_Appearance_android_textColor)
    }

    override fun setTextColor(colors: ColorStateList?) {
        if (!_inited)
            return super.setTextColor(colors)
        _appearance.textColor = colors
        syncAppearanceV(R.styleable.ZButton_Appearance_android_textColor)
    }

    override fun setTextSize(unit: Int, size: Float) {
        if (!_inited || unit != TypedValue.COMPLEX_UNIT_PX)
            return super.setTextSize(unit, size)
        _appearance.textSize = size
        syncAppearanceV(R.styleable.ZButton_Appearance_android_textSize)
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
        val padding = _appearance.paddingX
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
        syncAppearance(updateColor())
    }

    /* private */

    companion object {

        private const val TAG = "ZButton"

        private fun backgroundDrawable(appearance: ZButtonAppearance) : Drawable? {
            val backgroundColor = appearance.backgroundColor ?: return null
            return RoundDrawable(backgroundColor, appearance.cornerRadius)
        }

    }

    @SuppressLint("CustomViewStyleable")
    private fun readStyle(a: TypedArray) {
        var changed = 0
        val ba = a.getResourceId(R.styleable.ZButton_buttonAppearance, 0)
        if (ba != 0) {
            changed = _appearance.setButtonApperanceId(context, ba)
        }
        content = a.getResourceId(R.styleable.ZButton_content, 0)
        changed = changed or _appearance.readAppearance(context, a, true)
        icon = a.getResourceId(R.styleable.ZButton_icon, 0)
        loadingIcon = a.getResourceId(R.styleable.ZButton_loadingIcon, 0)
        loadingText = a.getText(R.styleable.ZButton_loadingText)
        iconPosition = IconPosition.values()[a.getInt(R.styleable.ZButton_iconPosition, iconPosition.ordinal)]
        if (_inited)
            syncAppearance(changed)
    }

    private fun updateColor() : Int {
        if (_appearance.textColorId != 0)
            _appearance.textColor = ContextCompat.getColorStateList(context, _appearance.textColorId)
        if (_appearance.backgroundColorId != 0)
            _appearance.backgroundColor = ContextCompat.getColorStateList(context, _appearance.backgroundColorId)
        return ZButtonAppearance.makeChange(R.styleable.ZButton_Appearance_android_textColor, R.styleable.ZButton_Appearance_backgroundColor)
    }

    private fun setTextInner(text: CharSequence?, type: BufferType = BufferType.NORMAL) {
        super.setText(text, type)
        if (_inited)
            syncIconPadding()
    }

    private fun syncAppearanceV(vararg indexes : Int) {
        syncAppearance(ZButtonAppearance.makeChange(*indexes))
    }

    private fun syncAppearance(changed : Int = 0xffff) {
        if (ZButtonAppearance.hasChanged(changed,
                R.styleable.ZButton_Appearance_cornerRadius,
                R.styleable.ZButton_Appearance_backgroundColor)) {
            background = backgroundDrawable(_appearance)?.toGradient(this)
        }
        if (ZButtonAppearance.hasChanged(changed,
                R.styleable.ZButton_Appearance_android_textColor)) {
            super.setTextColor(_appearance.textColor?.toGradient(this))
        }
        if (ZButtonAppearance.hasChanged(changed,
                R.styleable.ZButton_Appearance_android_textColor,
                R.styleable.ZButton_Appearance_iconColor)) {
            val color = _appearance.iconColor ?: textColors
            if (Drawables.isPureColor(_icon)) {
                _icon?.setTintList(color)
                GradientColorList.progress(color)?.add(_icon!!) {
                    it.state = intArrayOf()
                }
            }
        }
        if (ZButtonAppearance.hasChanged(changed,
                R.styleable.ZButton_Appearance_android_textSize)) {
            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, _appearance.textSize)
        }
        if (ZButtonAppearance.hasChanged(changed,
                R.styleable.ZButton_Appearance_lineHeight)) {
//            super.setLineHeight(_appearance.lineHeight.toInt())
        }
        if (ZButtonAppearance.hasChanged(changed,
                R.styleable.ZButton_Appearance_iconPosition)) {
                    syncCompoundDrawable(true)
        }
        if (ZButtonAppearance.hasChanged(changed,
                R.styleable.ZButton_Appearance_android_minHeight)) {
            minHeight = _appearance.minHeight
        }
        if (ZButtonAppearance.hasChanged(changed,
                R.styleable.ZButton_Appearance_paddingX,
                R.styleable.ZButton_Appearance_paddingY)) {
            val paddingX = _appearance.paddingX
            val paddingY = _appearance.paddingY
            if (_inited)
                setPadding(paddingX, paddingY, paddingX, paddingY)
            else
                setPadding(paddingLeft + paddingX, paddingTop + paddingY, paddingRight + paddingX, paddingBottom + paddingY)
        }
        if (ZButtonAppearance.hasChanged(changed,
                R.styleable.ZButton_Appearance_iconSize)) {
            val iconSize = _appearance.iconSize
            _icon?.setBounds(0, 0, iconSize, iconSize)
            _loadingIcon?.setBounds(0, 0, iconSize, iconSize)
            // fix size change, icon not at center
            syncCompoundDrawable(true)
        }
        if (ZButtonAppearance.hasChanged(changed,
                R.styleable.ZButton_Appearance_iconPadding)) {
            syncIconPadding()
        }
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
                val stub = context.obtainStyledAttributes(content, intArrayOf(R.attr.buttonAppearanceStub))
                if (stub.getBoolean(0, false)) {
                    buttonAppearance = content
                } else {
                    icon = 0
                    text = null
                    val typedArray = context.obtainStyledAttributes(content, R.styleable.ZButton)
                    readStyle(typedArray)
                    typedArray.recycle()
                }
                stub.recycle()
            }
        }
    }

     private fun syncIcon(loading: Boolean = false) {
        val icon = if (loading) _loadingIcon else _icon
        if (icon != null) {
            icon.mutate()
            if (Drawables.isPureColor(icon)) {
                icon.setTintList(textColors)
                GradientColorList.progress(textColors)?.add(icon) {
                    it.state = intArrayOf()
                }
            }
            val iconSize = _appearance.iconSize
            icon.setBounds(0, 0, iconSize, iconSize)
        }
        if (loading == this.loading) {
            syncCompoundDrawable()
        }
    }

    private var _lastIcon: Drawable? = null

    private fun syncCompoundDrawable(position: Boolean = false) {
        val o = _lastIcon
        val d = if (loading) _loadingIcon else _icon
        if (o == d && !position)
            return
        // How to center icon and text in a android button with width set to “fill parent”
        // https://stackoverflow.com/questions/3634191/how-to-center-icon-and-text-in-a-android-button-with-width-set-to-fill-parent
        val left = if (iconPosition == IconPosition.Left) d else null
        val top = if (iconPosition == IconPosition.Top) d else null
        val right = if (iconPosition == IconPosition.Right) d else null
        val bottom = if (iconPosition == IconPosition.Bottom) d else null
        // setCompoundDrawablesRelative has measure problem on TextView
        //  --Relative calc sizes of start/end, but measure use left/right sizes
        setCompoundDrawables(left, top, right, bottom)
        if (o is Animatable)
            o.stop()
        if (d is Animatable)
            d.start()
        _lastIcon = d
    }

    private fun syncIconPadding() {
        compoundDrawablePadding = if (text.count() == 0) 0 else _appearance.iconPadding
    }

}
