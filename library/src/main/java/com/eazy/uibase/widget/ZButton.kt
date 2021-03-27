package com.eazy.uibase.widget

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.eazy.uibase.R
import com.eazy.uibase.resources.ShapeDrawables

class ZButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatButton(context, attrs)
{
    enum class ButtonType {
        Primitive, Secondary, Tertiary, Danger, Text
    }

    enum class ButtonSize {
        Large, Middle, Small, Thin
    }

    companion object {

        data class TypeStyles(@ColorRes val textColor: Int, @ColorRes val backgroundColor: Int)
        data class SizeStyles(@DimenRes val height: Int, @DimenRes val radius: Int,
                              @DimenRes val padding: Int, @DimenRes val textSize: Int, @DimenRes val iconPadding: Int)

        private val typeStyles: Map<ButtonType, TypeStyles> = mapOf(
            ButtonType.Primitive to TypeStyles(R.color.bluegrey900_disabled, R.color.brand500_pressed_disabled),
            ButtonType.Secondary to TypeStyles(R.color.blue600_disabled, R.color.blue100_pressed_disabled),
            ButtonType.Tertiary to TypeStyles(R.color.bluegrey800_disabled, R.color.bluegrey100_pressed_disabled),
            ButtonType.Danger to TypeStyles(R.color.red600_disabled, R.color.red100_pressed_disabled),
            ButtonType.Text to TypeStyles(R.color.blue600_disabled, R.color.transparent_pressed_disabled),
        )

        private val sizeStyles: Map<ButtonSize, SizeStyles> = mapOf(
            ButtonSize.Large to SizeStyles(R.dimen.button_height_large, R.dimen.button_radius_large,
                R.dimen.button_padding_large, R.dimen.button_textSize_large, R.dimen.button_iconPadding_large),
            ButtonSize.Middle to SizeStyles(R.dimen.button_height_middle, R.dimen.button_radius_middle,
                R.dimen.button_padding_middle, R.dimen.button_textSize_middle, R.dimen.button_iconPadding_middle),
            ButtonSize.Small to SizeStyles(R.dimen.button_height_small, R.dimen.button_radius_small,
                R.dimen.button_padding_small, R.dimen.button_textSize_small, R.dimen.button_iconPadding_small),
            ButtonSize.Thin to SizeStyles(R.dimen.button_height_small, 0,
                0, R.dimen.button_textSize_small, R.dimen.button_iconPadding_small),
        )

        private fun backgroundDrawable(context: Context, type: ButtonType, size: ButtonSize) : Drawable {
            return backgroundDrawable(context, typeStyles[type]!!, sizeStyles[size]!!)
        }

        private fun backgroundDrawable(context: Context, types: TypeStyles, sizes: SizeStyles) : Drawable {
            // it's stateful, so can't shard
            return createBackgroundDrawable(context, types, sizes)
        }

        private fun createBackgroundDrawable(context: Context, types: TypeStyles, sizes: SizeStyles) : Drawable {
//            return DrawableBuilder().apply {
//                rectangle()
//                solidColorStateList(context.resources.getColorStateList(types.backgroundColor))
//                cornerRadius(context.resources.getDimensionPixelSize(sizes.radius))
//            }.build();
            val config = ShapeDrawables.Config(GradientDrawable.RECTANGLE, sizes.radius, types.backgroundColor,
                0, 0, 0, 0)
            return ShapeDrawables.getDrawable(context, config)
        }
    }

    private var _icon: Drawable? = null
    private var _text: CharSequence? = null

    var buttonType: ButtonType = ButtonType.Primitive
        set(value) {
            if (field == value)
                return
            field = value
            updateType()
        }

    var buttonSize: ButtonSize = ButtonSize.Large
        set(value) {
            if (field == value)
                return
            field = value
            updateSize()
        }

    var icon: Int? = null
        set(value) {
            if (field == value)
                return
            field = value
            _icon = if (value != null && value > 0)
                ContextCompat.getDrawable(context, value)!!
            else
                null
            applyIcon()
        }

    var iconAtRight = false
        set(value) {
            if (field == value)
                return
            field = value
            applyIcon()
        }

    var loadingDrawable: Drawable? = null
        set(value) {
            field = value
            if (loading)
                setCompoundDrawablesWithIntrinsicBounds(value, null, null, null)
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
            val o = if (loading) _icon else loadingDrawable
            val d = if (loading) loadingDrawable else _icon
            // How to center icon and text in a android button with width set to “fill parent”
            // https://stackoverflow.com/questions/3634191/how-to-center-icon-and-text-in-a-android-button-with-width-set-to-fill-parent
            val left = if (iconAtRight) null else d
            val right = if (iconAtRight) d else null
            setCompoundDrawablesWithIntrinsicBounds(left, null, right, null)
            if (d is Animatable)
                d.start()
            if (o is Animatable)
                o.stop()
            // swap text
            val t = if (loading) loadingText else _text
            setTextInner(t)
        }

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.ZButton,
                    R.attr.buttonStyle, 0)
            val type = a.getInt(R.styleable.ZButton_buttonType, -1)
            if (type >= 0 && type != buttonType.ordinal)
                buttonType = ButtonType.values()[type]
            else
                updateType()
            val size = a.getInt(R.styleable.ZButton_buttonSize, -1)
            if (size >= 0 && size != buttonSize.ordinal)
                buttonSize = ButtonSize.values()[size]
            else
                updateSize()
            icon = a.getResourceId(R.styleable.ZButton_icon, 0)
            loadingDrawable = a.getDrawable(R.styleable.ZButton_loadingDrawable)
            loadingText = a.getText(R.styleable.ZButton_loadingText)
            a.recycle()
        }
        applyIcon()
    }

    override fun setText(text: CharSequence?, type: BufferType) {
        _text = text
        if (!loading)
            setTextInner(text, type)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        if (widthMode != MeasureSpec.EXACTLY) {
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
        val sizes = sizeStyles[buttonSize]!!
        val padding = context.resources.getDimensionPixelSize(sizes.padding)
        setPadding(padding, 0, padding, 0)
    }

    private fun setTextInner(text: CharSequence?, type: BufferType = BufferType.NORMAL) {
        super.setText(text, type)
        if (buttonSize != null)
            updateIconPadding(sizeStyles[buttonSize]!!)
    }

    private fun updateType() {
        background = backgroundDrawable(context, buttonType, buttonSize)
        val types = typeStyles[buttonType]!!
        setTextColor(AppCompatResources.getColorStateList(context, (types.textColor)))
        _icon?.setTintList(textColors)
    }

    private fun updateSize() {
        background = backgroundDrawable(context, buttonType, buttonSize)
        val sizes = sizeStyles[buttonSize]!!
        height = context.resources.getDimensionPixelSize(sizes.height)
        val padding = if (sizes.padding > 0) context.resources.getDimensionPixelSize(sizes.padding) else 0
        setPadding(padding, 0, padding, 0)
        setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(sizes.textSize))
        updateIconPadding(sizes)
    }

    private fun updateIconPadding(sizes: SizeStyles) {
        compoundDrawablePadding = if (text.count() == 0) 0 else context.resources.getDimensionPixelSize(sizes.iconPadding)
    }

    private fun applyIcon() {
        val icon = _icon
        if (icon != null) {
            icon.setTintMode(PorterDuff.Mode.SRC_IN)
            icon.setTintList(textColors)
        }
        if (!loading) {
            val left = if (iconAtRight) null else icon
            val right = if (iconAtRight) icon else null
            setCompoundDrawablesWithIntrinsicBounds(left, null, right, null)
        }
    }

}
