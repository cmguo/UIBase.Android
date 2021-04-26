package com.eazy.uibase.view

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView
import kotlin.math.min

class MaxHeightScrollView @JvmOverloads constructor (
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {

    private val maxHeight: Int

    init {
        val styleableSet = intArrayOf(android.R.attr.maxHeight)
        val a = context.obtainStyledAttributes(attrs, styleableSet, defStyleAttr, 0)
        maxHeight = a.getDimensionPixelSize(0, 0);
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        if (maxHeight > 0) {
            val hSize = MeasureSpec.getSize(heightMeasureSpec)
            when (MeasureSpec.getMode(heightMeasureSpec)) {
                MeasureSpec.AT_MOST -> heightMeasureSpec = MeasureSpec.makeMeasureSpec(min(hSize, maxHeight), MeasureSpec.AT_MOST)
                MeasureSpec.UNSPECIFIED -> heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST)
                MeasureSpec.EXACTLY -> heightMeasureSpec = MeasureSpec.makeMeasureSpec(min(hSize, maxHeight), MeasureSpec.EXACTLY)
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

}