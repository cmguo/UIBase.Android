package com.eazy.uibase.resources

import android.animation.ArgbEvaluator
import android.content.res.ColorStateList
import android.graphics.drawable.ColorStateListDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.VectorDrawable
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.eazy.uibase.R
import com.eazy.uibase.view.getTagInTree
import java.lang.reflect.Method

class GradientColorList(private val colorList: ColorStateList, val progress: GradientProgress)
    : ColorStateList(arrayOf(), intArrayOf()) {

    override fun getDefaultColor(): Int {
        return colorList.defaultColor
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun getChangingConfigurations(): Int {
        return colorList.changingConfigurations
    }

    override fun getColorForState(stateSet: IntArray?, defaultColor: Int): Int {
        var color = colorList.getColorForState(stateSet, defaultColor)
        if (stateSet == null)
            return color
        val progress = progress.progress
        if (progress < 0) {
            val stateSet2 = stateSet.copyOf(stateSet.size + 1)
            stateSet2[stateSet.size] = R.attr.state_gradient_start
            val color2 = colorList.getColorForState(stateSet2, color)
            color = evaluator.evaluate(-progress, color, color2) as Int
        } else if (progress > 0) {
            val stateSet2 = stateSet.copyOf(stateSet.size + 1)
            stateSet2[stateSet.size] = R.attr.state_gradient_end
            val color2 = colorList.getColorForState(stateSet2, color)
            color = evaluator.evaluate(progress, color, color2) as Int
        }
        return color
    }

    override fun isStateful(): Boolean {
        return true
    }

    companion object {

        var evaluator = ArgbEvaluator()

        private var _hasStateChecked = false
        private var _hasState : Method? = null

        fun hasGradient(colorList: ColorStateList) : Boolean {
            if (!_hasStateChecked) {
                _hasStateChecked = true
                try {
                    _hasState = ColorStateList::class.java.getMethod("hasState", Integer.TYPE)
                } catch (_: Throwable) {}
            }
            val hasState = _hasState
            if (hasState == null) {
                val color = colorList.defaultColor
                return colorList.getColorForState(intArrayOf(R.attr.state_gradient_start), color) != color
                    || colorList.getColorForState(intArrayOf(R.attr.state_gradient_end), color) != color
            }
            return try {
                (hasState.invoke(colorList, R.attr.state_gradient_start) as? Boolean ?: false)
                    || (hasState.invoke(colorList, R.attr.state_gradient_end) as? Boolean ?: false)
            } catch (_: Throwable) {
                false
            }
        }

        fun prepare(view: View) {
            view.setTag(R.id.gradient_progress, GradientProgress())
        }

        fun setProgress(view: View, process: Float) {
            (view.getTag(R.id.gradient_progress) as GradientProgress).progress = process
        }

        fun progress(vararg colorLists: ColorStateList?) : GradientProgress? {
            for (colorList in colorLists) {
                if (colorList is GradientColorList) {
                    return colorList.progress
                }
            }
            return null
        }

    }
}

fun ColorStateList.toGradient(view: View, drawable: Boolean = false) : ColorStateList {
    if (GradientColorList.hasGradient(this)) {
        val progress : GradientProgress = view.getTagInTree(R.id.gradient_progress) ?: return this
        if (!drawable)
            progress.add(view)
        return GradientColorList(this, progress)
    }
    return this
}

fun Drawable.toGradient(view: View) : Drawable {
    if (this is RoundDrawable) {
        fillColor = fillColor?.toGradient(view, true)
        borderColor = borderColor?.toGradient(view, true)
        GradientColorList.progress(fillColor, borderColor)?.add(this, Drawable::invalidateSelf)
        return this
    }
    val drawable = mutate()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && drawable is ColorStateListDrawable) {
        drawable.colorStateList = drawable.colorStateList.toGradient(view)
        /*
            ColorStateListDrawable.setColorStateList
                Drawable.onStateChange
         */
        GradientColorList.progress(drawable.colorStateList)?.add(this) {
            val drawable2 = (it as ColorStateListDrawable)
            drawable2.colorStateList = drawable2.colorStateList
        }
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && drawable is GradientDrawable) {
        drawable.color = drawable.color?.toGradient(view)
        /*
            GradientDrawable.setColor
                Drawable.onStateChange
         */
        GradientColorList.progress(drawable.color)?.add(this) {
            val drawable2 = (it as GradientDrawable)
            drawable2.color = drawable2.color
        }
    }
    return drawable
}

class GradientProgress {

    var progress: Float = 0.0f
        set(value) {
            field = value
            for (d in drawables) {
                d.value(d.key)
            }
            /*
                View.refreshDrawableState
                    View.drawableStateChanged
                        View.getDrawableState
                        Drawable.setState
                        View.invalidate (if changed)
             */
            for (v in views) {
                v.refreshDrawableState()
                v.invalidate()
            }
        }

    private val views = arrayListOf<View>()
    private val drawables = mutableMapOf<Drawable, (Drawable) -> Unit>()

    fun add(view: View) {
        if (!views.contains(view))
            views.add(view)
    }
    fun remove(view: View) {
        views.remove(view)
    }
    fun add(drawable: Drawable, refresh: (Drawable) -> Unit) {
        drawables[drawable] = refresh
    }
    fun remove(drawable: Drawable) {
        drawables.remove(drawable)
    }

}
