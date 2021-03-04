package com.eazy.uibase.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.appcompat.widget.AppCompatButton
import com.eazy.uibase.R
import top.defaults.drawabletoolbox.DrawableBuilder

public class ZButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr)
{
    enum class ButtonType {
        Primitive, Secondary, Tertiary, Danger, Text
    }

    enum class ButtonSize {
        Small, Middle, Large
    }

    companion object {

        data class TypeStyles(@ColorRes val textColor: Int, @ColorRes val backgroundColor : Int)
        data class SizeStyles(@DimenRes val height: Int, @DimenRes val radius : Int,
                              @DimenRes val padding : Int, @DimenRes val textSize : Int, @DimenRes val iconPadding : Int)

        private val typeStyles: Map<ButtonType, TypeStyles> = mapOf(
            ButtonType.Primitive to TypeStyles(R.color.bluegrey900_disabled, R.color.brand500_pressed_disabled),
            ButtonType.Secondary to TypeStyles(R.color.blue600_disabled, R.color.blue100_pressed_disabled),
            ButtonType.Tertiary to TypeStyles(R.color.bluegrey800_disabled, R.color.bluegrey100_pressed_disabled),
            ButtonType.Danger to TypeStyles(R.color.red600_disabled, R.color.red100_pressed_disabled),
            ButtonType.Text to TypeStyles(R.color.blue600_disabled, R.color.transparent_pressed_disabled),
        )

        private val sizeStyles: Map<ButtonSize, SizeStyles> = mapOf(
            ButtonSize.Small to SizeStyles (R.dimen.button_heigth_small, R.dimen.button_radius_small,
                R.dimen.button_padding_small, R.dimen.button_textSize_small, R.dimen.button_iconPadding_small),
            ButtonSize.Middle to SizeStyles (R.dimen.button_heigth_middle, R.dimen.button_radius_middle,
                R.dimen.button_padding_middle, R.dimen.button_textSize_middle, R.dimen.button_iconPadding_middle),
            ButtonSize.Large to SizeStyles(R.dimen.button_heigth_large, R.dimen.button_radius_large,
                R.dimen.button_padding_large, R.dimen.button_textSize_large, R.dimen.button_iconPadding_large),
        )

        val backgroundDrawables: MutableMap<TypeStyles, MutableMap<SizeStyles, Drawable>> = mutableMapOf()

        fun backgroundDrawable(context: Context, type: ButtonType, size: ButtonSize) : Drawable {
            return backgroundDrawable(context, typeStyles[type]!!, sizeStyles[size]!!)
        }

        fun backgroundDrawable(context: Context, types: TypeStyles, sizes: SizeStyles) : Drawable {
            return backgroundDrawables.getOrPut(types) {
                mutableMapOf();
            }.getOrPut(sizes) {
                createBackgroundDrawable(context, types, sizes)
            }
        }

        fun createBackgroundDrawable(context: Context, types: TypeStyles, sizes: SizeStyles) : Drawable {
//            return DrawableBuilder().apply {
//                rectangle()
//                solidColorStateList(context.resources.getColorStateList(types.backgroundColor))
//                cornerRadius(context.resources.getDimensionPixelSize(sizes.radius))
//            }.build();
            val drawable = GradientDrawable()
            drawable.shape = GradientDrawable.RECTANGLE
            drawable.cornerRadius = context.resources.getDimension(sizes.radius)
            drawable.color = context.resources.getColorStateList(types.backgroundColor)
            return drawable
        }
    }

    private var type_ = ButtonType.Primitive
    private var size_ = ButtonSize.Large
    private var loadingDrawable_: Drawable? = null
    private var loadingText_: CharSequence? = null
    private var loading_ = false

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.ZButton, 0, R.style.ZButton)
            val type = a.getInt(R.styleable.ZButton_buttonType, -1)
            if (type >= 0)
                type_ = ButtonType.values()[type]
            val size = a.getInt(R.styleable.ZButton_buttonSize, -1)
            if (size >= 0)
                size_ = ButtonSize.values()[size]
            loadingDrawable_ = a.getDrawable(R.styleable.ZButton_loadingDrawable2)
            loadingText_ = a.getText(R.styleable.ZButton_loadingText2)
            a.recycle()
        }
        val types = typeStyles[type_]!!
        val sizes = sizeStyles[size_]!!
        background = backgroundDrawable(context, types, sizes)
        setTextColor(context.resources.getColor(types.textColor))
        height = context.resources.getDimensionPixelSize(sizes.height)
        val padding = context.resources.getDimensionPixelSize(sizes.padding)
        setPadding(padding, 0, padding, 0)
        setTextSize(TypedValue.COMPLEX_UNIT_PX,  context.resources.getDimension(sizes.textSize))
        setCompoundDrawablePadding(context.resources.getDimensionPixelSize(sizes.iconPadding))
    }

    var buttonType: ButtonType
        get() = type_
        set(value) {
            if (type_ == value)
                return
            type_ = value
            background = backgroundDrawable(context, type_, size_)
            val types = typeStyles[type_]!!
            setTextColor(context.resources.getColor(types.textColor))
        }

    var buttonSize: ButtonSize
        get() = size_
        set(value) {
            if (size_ == value)
                return
            size_ = value
            background = backgroundDrawable(context, type_, size_)
            val sizes = sizeStyles[size_]!!
            height = context.resources.getDimensionPixelSize(sizes.height)
            val padding = context.resources.getDimensionPixelSize(sizes.padding)
            setPadding(padding, 0, padding, 0)
            setTextSize(TypedValue.COMPLEX_UNIT_PX,  context.resources.getDimension(sizes.textSize))
            setCompoundDrawablePadding(context.resources.getDimensionPixelSize(sizes.iconPadding))
        }

    var loadingDrawable: Drawable?
        get() = loadingDrawable_
        set(value) {
            loadingDrawable_ = value
            if (loading_)
                setCompoundDrawables(loadingDrawable_, null, null, null)
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
            val d = compoundDrawables[0]
            setCompoundDrawables(loadingDrawable_, null, null, null)
            loadingDrawable_ = d
            // swap text
            val t = text
            text = loadingText_
            loadingText_ = t
        }

}
