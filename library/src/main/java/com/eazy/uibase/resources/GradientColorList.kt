package com.eazy.uibase.resources

import android.animation.ArgbEvaluator
import android.content.res.ColorStateList
import android.graphics.drawable.ColorStateListDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.eazy.uibase.R
import com.eazy.uibase.view.getTagInTree
import java.lang.reflect.Method

class GradientProgress {

    var progress: Float = 0.0f
        set(value) {
            field = value
            for (d in drawables) {
                d.state = intArrayOf()
            }
            for (v in views) {
                v.refreshDrawableState()
                v.invalidate()
            }
        }

    private val views = arrayListOf<View>()
    private val drawables = arrayListOf<Drawable>()

    fun add(view: View) {
        if (!views.contains(view))
            views.add(view)
    }

    fun remove(view: View) {
        views.remove(view)
    }

    fun add(drawable: Drawable) {
        if (!drawables.contains(drawable))
            drawables.add(drawable)
    }

    fun remove(drawable: Drawable) {
        drawables.remove(drawable)
    }


}

class GradientColorList(private val colorList: ColorStateList, private val progress: GradientProgress)
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

        private val _hasState : Method? = ColorStateList::class.java.getMethod("hasState", Integer.TYPE)

        fun hasGradient(colorList: ColorStateList) : Boolean {
            if (_hasState == null)
                return false
            return try {
                (_hasState.invoke(colorList, R.attr.state_gradient_start) as? Boolean ?: false)
                    || (_hasState.invoke(colorList, R.attr.state_gradient_end) as? Boolean ?: false)
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
    }
}

fun ColorStateList.toGradient(view: View, drawable: Drawable? = null) : ColorStateList {
    if (GradientColorList.hasGradient(this)) {
        val progress : GradientProgress = view.getTagInTree(R.id.gradient_progress) ?: return this
        progress.add(view)
        if (drawable != null)
            progress.add(drawable)
        return GradientColorList(this, progress)
    }
    return this
}

fun Drawable.toGradient(view: View) : Drawable {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && this is ColorStateListDrawable) {
        val drawable = mutate() as ColorStateListDrawable
        drawable.colorStateList = colorStateList.toGradient(view, this)
        return drawable
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && this is GradientDrawable) {
        val drawable = mutate() as GradientDrawable
        drawable.color = color?.toGradient(view, this)
        return drawable
    }
    return this
}
