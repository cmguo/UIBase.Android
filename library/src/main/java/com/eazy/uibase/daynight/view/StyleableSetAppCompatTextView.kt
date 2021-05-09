package com.eazy.uibase.daynight.view

import android.util.TypedValue
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatTextView
import com.google.auto.service.AutoService
import com.eazy.uibase.R
import com.eazy.uibase.daynight.styleable.IStyleableSet
import com.eazy.uibase.daynight.styleable.StyleableSet

@AutoService(IStyleableSet::class)
class StyleableSetAppCompatTextView : StyleableSet<AppCompatTextView>() {

    override fun defStyleAttr(): Int {
        return android.R.attr.textViewStyle
    }

    init {
        addStyleable(R.attr.drawableLeftCompat) { view: AppCompatTextView, value: TypedValue ->
            setCompoundDrawable(view, 0, value)
        }
        addStyleable(R.attr.drawableTopCompat) { view: AppCompatTextView, value: TypedValue ->
            setCompoundDrawable(view, 1, value)
        }
        addStyleable(R.attr.drawableRightCompat) { view: AppCompatTextView, value: TypedValue ->
            setCompoundDrawable(view, 2, value)
        }
        addStyleable(R.attr.drawableBottomCompat) { view: AppCompatTextView, value: TypedValue ->
            setCompoundDrawable(view, 3, value)
        }
        addStyleable(R.attr.drawableStartCompat) { view: AppCompatTextView, value: TypedValue ->
            setCompoundDrawableRelative(view, 0, value)
        }
        addStyleable(R.attr.drawableEndCompat) { view: AppCompatTextView, value: TypedValue ->
            setCompoundDrawableRelative(view, 2, value)
        }
    }


    private fun setCompoundDrawable(view: AppCompatTextView, index: Int, value: TypedValue) {
        val drawables = view.compoundDrawables
        drawables[index] = AppCompatResources.getDrawable(view.context, value.resourceId)
        view.setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], drawables[3])
    }

    private fun setCompoundDrawableRelative(view: AppCompatTextView, index: Int, value: TypedValue) {
        val drawables = view.compoundDrawablesRelative
        drawables[index] = AppCompatResources.getDrawable(view.context, value.resourceId)
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], drawables[3])
    }
}
