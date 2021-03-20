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
import androidx.appcompat.widget.AppCompatButton
import androidx.core.graphics.drawable.DrawableCompat
import com.eazy.uibase.R
import com.eazy.uibase.resources.ShapeDrawables

public class ZButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatButton(context, attrs)
{
    enum class ButtonType {
        Primitive, Secondary, Tertiary, Danger, Text
    }

    enum class ButtonSize {
        Small, Middle, Large
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
            ButtonSize.Small to SizeStyles(R.dimen.button_height_small, R.dimen.button_radius_small,
                R.dimen.button_padding_small, R.dimen.button_textSize_small, R.dimen.button_iconPadding_small),
            ButtonSize.Middle to SizeStyles(R.dimen.button_height_middle, R.dimen.button_radius_middle,
                R.dimen.button_padding_middle, R.dimen.button_textSize_middle, R.dimen.button_iconPadding_middle),
            ButtonSize.Large to SizeStyles(R.dimen.button_height_large, R.dimen.button_radius_large,
                R.dimen.button_padding_large, R.dimen.button_textSize_large, R.dimen.button_iconPadding_large),
        )

        fun backgroundDrawable(context: Context, type: ButtonType, size: ButtonSize) : Drawable {
            return backgroundDrawable(context, typeStyles[type]!!, sizeStyles[size]!!)
        }

        fun backgroundDrawable(context: Context, types: TypeStyles, sizes: SizeStyles) : Drawable {
            // it's stateful, so can't shard
            return createBackgroundDrawable(context, types, sizes)
        }

        fun createBackgroundDrawable(context: Context, types: TypeStyles, sizes: SizeStyles) : Drawable {
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

    private var icon_: Drawable? = null
    private var loadingDrawable_: Drawable? = null
    private var loadingText_: CharSequence? = null
    private var loading_ = false

    var buttonType: ButtonType = ButtonType.Primitive
        set(value) {
            if (field == value)
                return
            field = value
            background = backgroundDrawable(context, value, buttonSize)
            val types = typeStyles[value]!!
            setTextColor(context.resources.getColorStateList(types.textColor))
            icon_?.setTintList(textColors)
        }

    var buttonSize: ButtonSize = ButtonSize.Large
        set(value) {
            if (field == value)
                return
            field = value
            background = backgroundDrawable(context, buttonType, value)
            val sizes = sizeStyles[value]!!
            height = context.resources.getDimensionPixelSize(sizes.height)
            val padding = context.resources.getDimensionPixelSize(sizes.padding)
            setPadding(padding, 0, padding, 0)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(sizes.textSize))
            setCompoundDrawablePadding(context.resources.getDimensionPixelSize(sizes.iconPadding))
        }

    var icon: Int? = null
        set(value) {
            if (field == value)
                return
            field = value
            icon_ = if (value != null && value > 0)
                DrawableCompat.wrap(context.getDrawable(value)!!)
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

    var loadingDrawable: Drawable?
        get() = loadingDrawable_
        set(value) {
            loadingDrawable_ = value
            if (loading_)
                setCompoundDrawablesWithIntrinsicBounds(loadingDrawable_, null, null, null)
        }

    var loadingText: CharSequence?
        get() = loadingText_
        set(value) {
            if (loading_)
                text = value
            else
                loadingText_ = value
        }

    var loading: Boolean
        get() = loading_
        set(value) {
            if (loading_ == value)
                return
            loading_ = value
            // swap
            val d = loadingDrawable_;
            val o = compoundDrawables[0]
            // How to center icon and text in a android button with width set to “fill parent”
            // https://stackoverflow.com/questions/3634191/how-to-center-icon-and-text-in-a-android-button-with-width-set-to-fill-parent
            val left = if (iconAtRight) null else d
            val right = if (iconAtRight) d else null
            setCompoundDrawablesWithIntrinsicBounds(left, null, right, null)
            if (d is Animatable)
                d.start()
            if (o is Animatable)
                o.stop()
            loadingDrawable_ = o
            // swap text
            val t = text
            text = loadingText_
            loadingText_ = t
        }

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.ZButton,
                    R.attr.buttonStyle, 0)
            val type = a.getInt(R.styleable.ZButton_buttonType, -1)
            if (type >= 0)
                this.buttonType = ButtonType.values()[type]
            val size = a.getInt(R.styleable.ZButton_buttonSize, -1)
            if (size >= 0)
                this.buttonSize = ButtonSize.values()[size]
            //icon_ = a.getDrawable(R.styleable.ZButton_icon)
            this.icon = a.getResourceId(R.styleable.ZButton_icon, 0)
            loadingDrawable_ = a.getDrawable(R.styleable.ZButton_loadingDrawable)
            loadingText_ = a.getText(R.styleable.ZButton_loadingText)
            a.recycle()
        }
        val types = typeStyles[buttonType]!!
        val sizes = sizeStyles[buttonSize]!!
        background = backgroundDrawable(context, types, sizes)
        setTextColor(context.resources.getColorStateList(types.textColor))
        height = context.resources.getDimensionPixelSize(sizes.height)
        val padding = context.resources.getDimensionPixelSize(sizes.padding)
        setPadding(padding, 0, padding, 0)
        setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(sizes.textSize))
        setCompoundDrawablePadding(context.resources.getDimensionPixelSize(sizes.iconPadding))
        applyIcon()
    }

    private fun applyIcon() {
        val icon = icon_
        if (icon != null) {
            icon.setTintMode(PorterDuff.Mode.SRC_IN)
            icon.setTintList(textColors)
        }
        if (!loading_) {
            val left = if (iconAtRight) null else icon
            val right = if (iconAtRight) icon else null
            setCompoundDrawablesWithIntrinsicBounds(left, null, right, null)
        }
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

}
