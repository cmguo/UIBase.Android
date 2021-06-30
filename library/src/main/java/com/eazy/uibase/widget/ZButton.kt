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

    // store value from style or set manually, not affect by value in ButtonAppearance
    var buttonType: ButtonType = ButtonType.Primitive
        set(value) {
            if (field == value)
                return
            field = value
            if (_inited && buttonAppearance == 0) {
                syncAppearance(readAppearance(value.resId))
            }
        }

    // store value from style or set manually, not affect by value in ButtonAppearance
    var buttonSize: ButtonSize = ButtonSize.Large
        set(value) {
            if (field == value)
                return
            field = value
            if (_inited && buttonAppearance == 0) {
                syncAppearance(readAppearance(value.resId))
            }
        }

    var buttonAppearance: Int
        get() = _appearance.styleId
        set(value) {
            if (_appearance.styleId == value)
                return
            _appearance.styleId = value
            if (_inited && value != 0) {
                syncAppearance(readAppearance(value))
            }
        }

    var backgroundColor: ColorStateList?
        get() = _appearance.backgroundColor
        set(value) {
            _appearance.backgroundColor = value
            if (_inited)
                syncAppearance(makeChange(R.styleable.ZButton_Appearance_backgroundColor))
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
    private var _daftTypeSizeApplied = false

    private var _icon: Drawable? = null
    private var _loadingIcon: Drawable? = null
    private var _text: CharSequence? = null

    data class Appearance(
        @StyleRes
        var styleId: Int,
        var textColor: ColorStateList?, var backgroundColor: ColorStateList?,
        var iconPosition: IconPosition,
        var height: Int, var cornerRadius: Float, var padding: Int,
        var textSize: Float, var iconSize: Int, var iconPadding: Int)

    private val _appearance = Appearance(0,
        null, null, IconPosition.Left,
        0, 0f, 0, 0f, 0, 0)

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
        syncAppearance(makeChange(R.styleable.ZButton_Appearance_android_textColor))
    }

    override fun setTextColor(colors: ColorStateList?) {
        if (!_inited)
            return super.setTextColor(colors)
        _appearance.textColor = colors
        syncAppearance(makeChange(R.styleable.ZButton_Appearance_android_textColor))
    }

    override fun setTextSize(unit: Int, size: Float) {
        if (!_inited || unit != TypedValue.COMPLEX_UNIT_PX)
            return super.setTextSize(unit, size)
        _appearance.textSize = size
        syncAppearance(makeChange(R.styleable.ZButton_Appearance_android_textSize))
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
        val padding = _appearance.padding
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
        syncAppearance(makeChange())
    }

    /* private */

    companion object {

        private const val TAG = "ZButton"

        private fun backgroundDrawable(appearance: Appearance) : Drawable? {
            val backgroundColor = appearance.backgroundColor ?: return null
            return RoundDrawable(backgroundColor, appearance.cornerRadius)
        }

        private val sAppearanceValues = mapOf(
            R.styleable.ZButton_buttonType to R.styleable.ZButton_Appearance_buttonType,
            R.styleable.ZButton_buttonSize to R.styleable.ZButton_Appearance_buttonSize,
            R.styleable.ZButton_iconPosition to R.styleable.ZButton_Appearance_iconPosition,
            R.styleable.ZButton_backgroundColor to R.styleable.ZButton_Appearance_backgroundColor,
            R.styleable.ZButton_android_textColor to R.styleable.ZButton_Appearance_android_textColor,
            R.styleable.ZButton_android_textSize to R.styleable.ZButton_Appearance_android_textSize,
            R.styleable.ZButton_paddingX to R.styleable.ZButton_Appearance_paddingX,
            R.styleable.ZButton_height to R.styleable.ZButton_Appearance_height,
            R.styleable.ZButton_cornerRadius to R.styleable.ZButton_Appearance_cornerRadius,
            R.styleable.ZButton_iconSize to R.styleable.ZButton_Appearance_iconSize,
            R.styleable.ZButton_iconPadding to R.styleable.ZButton_Appearance_iconPadding
        )

        fun makeChange(vararg indexes : Int) : Int {
            var c = 0
            for (f in indexes) {
                c = c or (1 shl f)
            }
            return c
        }

        fun hasChanged(changed : Int, vararg indexes : Int) : Boolean {
            return (changed and makeChange(*indexes)) != 0
        }
    }

    @SuppressLint("CustomViewStyleable")
    private fun readStyle(a: TypedArray) {
        var changed = 0
        val ba = a.getResourceId(R.styleable.ZButton_buttonAppearance, _appearance.styleId)
        if (ba != 0 && ba != _appearance.styleId) {
            _appearance.styleId = ba
            val aa = context.obtainStyledAttributes(ba, R.styleable.ZButton_Appearance)
            changed = readAppearance(aa)
            aa.recycle()
        }
        content = a.getResourceId(R.styleable.ZButton_content, 0)
        changed = changed or readAppearance(a, true)
        icon = a.getResourceId(R.styleable.ZButton_icon, 0)
        loadingIcon = a.getResourceId(R.styleable.ZButton_loadingIcon, 0)
        loadingText = a.getText(R.styleable.ZButton_loadingText)
        iconPosition = IconPosition.values()[a.getInt(R.styleable.ZButton_iconPosition, iconPosition.ordinal)]
        if (_inited)
            syncAppearance(changed)
    }

    @SuppressLint("CustomViewStyleable")
    private fun readAppearance(@StyleRes id: Int) : Int {
        val aa = context.obtainStyledAttributes(id, R.styleable.ZButton_Appearance)
        val changed = readAppearance(aa)
        aa.recycle()
        return changed
    }

    private fun readAppearance(a: TypedArray, styleArray: Boolean = false) : Int {
        var changed = 0
        val n: Int = a.indexCount
        var type = if (_daftTypeSizeApplied) -1 else buttonType.ordinal
        var size = if (_daftTypeSizeApplied) -1 else buttonSize.ordinal
        _daftTypeSizeApplied = true
        for (i in 0 until n) {
            val attr: Int = a.getIndex(i)
            var index = attr
            if (styleArray) {
                index = sAppearanceValues[attr] ?: -1
                if (index == -1) {
                    continue
                }
            }
            when (index) {
                R.styleable.ZButton_Appearance_buttonType -> type = a.getInt(attr, type)
                R.styleable.ZButton_Appearance_buttonSize -> size = a.getInt(attr, size)
            }
        }
        // Always apply ButtonType & ButtonSize, because it's parts of ButtonAppearance
        if (type >= 0) {
            val bt = ButtonType.values()[type]
            if (styleArray && !_inited)
                buttonType = bt
            changed = changed or readAppearance(bt.resId)
        }
        if (size >= 0) {
            val bs = ButtonSize.values()[size]
            if (styleArray && !_inited)
                buttonSize = bs
            changed = changed or readAppearance(bs.resId)
        }
        for (i in 0 until n) {
            val attr: Int = a.getIndex(i)
            var index = attr
            if (styleArray) {
                index = sAppearanceValues[attr] ?: -1
                if (index == -1) {
                    continue
                }
            }
            when (index) {
                R.styleable.ZButton_Appearance_android_textColor ->
                    _appearance.textColor = a.getColorStateList(attr)
                R.styleable.ZButton_Appearance_backgroundColor ->
                    _appearance.backgroundColor = a.getColorStateList(attr)
                R.styleable.ZButton_Appearance_iconPosition -> {
                    val p = a.getInt(attr, _appearance.iconPosition.ordinal)
                    if (p != _appearance.iconPosition.ordinal) {
                        _appearance.iconPosition = IconPosition.values()[p]
                    } else {
                        index = -1 // not changed
                    }
                }
                R.styleable.ZButton_Appearance_height -> {
                    val h = a.getDimensionPixelSize(attr, _appearance.height)
                    if (h == _appearance.height) {
                        index = -1
                    } else {
                        _appearance.height = h
                    }
                }
                R.styleable.ZButton_Appearance_cornerRadius -> {
                    val d = a.getDimension(attr, _appearance.cornerRadius)
                    if (d == _appearance.cornerRadius) {
                        index = -1
                    } else {
                        _appearance.cornerRadius = d
                    }
                }
                R.styleable.ZButton_Appearance_paddingX -> {
                    val d = a.getDimensionPixelSize(attr, _appearance.padding)
                    if (d == _appearance.padding) {
                        index = -1
                    } else {
                        _appearance.padding = d
                    }
                }
                R.styleable.ZButton_Appearance_android_textSize -> {
                    val d = a.getDimension(attr, _appearance.textSize)
                    if (d == _appearance.textSize) {
                        index = -1
                    } else {
                        _appearance.textSize = d
                    }
                }
                R.styleable.ZButton_Appearance_iconSize -> {
                    val d = a.getDimensionPixelSize(attr, _appearance.iconSize)
                    if (d == _appearance.iconSize) {
                        index = -1
                    } else {
                        _appearance.iconSize = d
                    }
                }
                R.styleable.ZButton_Appearance_iconPadding -> {
                    val d = a.getDimensionPixelSize(attr, _appearance.iconPadding)
                    if (d == _appearance.iconPadding) {
                        index = -1
                    } else {
                        _appearance.iconPadding = d
                    }
                }
            } // when
            if (index != -1) {
                changed = changed or (1 shl index)
            }
        } // for
        return changed
    }

    private fun setTextInner(text: CharSequence?, type: BufferType = BufferType.NORMAL) {
        super.setText(text, type)
        if (_inited)
            syncIconPadding()
    }

    private fun syncAppearance(changed : Int = 0xffff) {
        if (hasChanged(changed,
                R.styleable.ZButton_Appearance_cornerRadius,
                R.styleable.ZButton_Appearance_backgroundColor)) {
            background = backgroundDrawable(_appearance)?.toGradient(this)
        }
        if (hasChanged(changed,
                R.styleable.ZButton_Appearance_android_textColor)) {
            super.setTextColor(_appearance.textColor?.toGradient(this))
            if (Drawables.isPureColor(_icon)) {
                _icon?.setTintList(textColors)
                GradientColorList.progress(textColors)?.add(_icon!!) {
                    it.state = intArrayOf()
                }
            }
        }
        if (hasChanged(changed,
                R.styleable.ZButton_Appearance_android_textSize)) {
            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, _appearance.textSize)
        }
        if (hasChanged(changed,
                R.styleable.ZButton_Appearance_iconPosition)) {
                    syncCompoundDrawable(true)
        }
        if (hasChanged(changed,
                R.styleable.ZButton_Appearance_height)) {
            height = _appearance.height
        }
        if (hasChanged(changed,
                R.styleable.ZButton_Appearance_paddingX)) {
            val padding = _appearance.padding
            setPadding(padding, 0, padding, 0)
        }
        if (hasChanged(changed,
                R.styleable.ZButton_Appearance_iconSize)) {
            val iconSize = _appearance.iconSize
            _icon?.setBounds(0, 0, iconSize, iconSize)
            _loadingIcon?.setBounds(0, 0, iconSize, iconSize)
        }
        if (hasChanged(changed,
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
