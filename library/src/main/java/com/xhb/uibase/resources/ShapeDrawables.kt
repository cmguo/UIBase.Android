package com.xhb.uibase.resources

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes

class ShapeDrawables {

    data class Config (val shape: Int,
                       @DimenRes val radius: Int, @ColorRes val fillColor: Int,
                       @DimenRes val borderSize: Int, @ColorRes val borderColor: Int,
                       @DimenRes val width: Int, @DimenRes val height: Int)

    companion object {

        private val drawables: MutableMap<Config, GradientDrawable> = mutableMapOf()

        fun getDrawable(context: Context, config: Config) : GradientDrawable {
//            return drawables.getOrPut(config) {
//                return createDrawable(context, config)
//            }
            var drawable = drawables.get(config)
            if (drawable == null) {
                drawable = createDrawable(context, config)
                if (!drawable.isStateful())
                    drawables.put(config, drawable)
            }
            return drawable
        }

        fun createDrawable(context: Context, config: Config) : GradientDrawable {
            val drawable = GradientDrawable()
            drawable.shape = config.shape
            if (config.width != 0 && config.height != 0) {
                val width = context.resources.getDimensionPixelSize(config.width)
                val height = context.resources.getDimensionPixelSize(config.height)
                drawable.setSize(width, height)
            }
            if (config.radius > 0)
                drawable.cornerRadius = context.resources.getDimension(config.radius)
            drawable.color = context.resources.getColorStateList(config.fillColor)
            if (config.borderSize != 0) {
                drawable.setStroke(context.resources.getDimensionPixelSize(config.borderSize),
                    context.resources.getColorStateList(config.borderColor))
            }
            return drawable
//            return DrawableBuilder().apply {
//                shape(config.shape)
//                val width = context.resources.getDimensionPixelSize(config.width)
//                val height = context.resources.getDimensionPixelSize(config.height)
//                size(width, height)
//                cornerRadius(context.resources.getDimensionPixelSize(config.radius))
//                solidColorStateList(context.resources.getColorStateList(config.fillColor))
//                //strokeWidth(context.resources.getDimensionPixelSize(config.borderSize))
//                strokeWidth(2)
//                strokeColorStateList(context.resources.getColorStateList(config.borderColor))
//            }.build()
        }
    }
}