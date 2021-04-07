package com.xhb.uibase.widget.tabbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.util.AttributeSet
import com.xhb.uibase.R
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.model.PositionData

class XHBRoundIndicator @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : WrapPagerIndicator(context, attrs, if (defStyleAttr == 0) R.attr.roundIndicatorStyle else defStyleAttr) {

    enum class WidthMode
    {
        MatchEdge,
        WrapContent,
        Exactly
    }

    var widthMode: WidthMode
        get() = WidthMode.values()[mode]
        set(value) {  mode = value.ordinal }

    var paddingX: Int
        get() = horizontalPadding + shadowRadius.toInt()
        set(value) { horizontalPadding = value - shadowRadius.toInt() }

    var paddingY: Int
        get() = verticalPadding + shadowRadius.toInt()
        set(value) { verticalPadding = value - shadowRadius.toInt() }

    var borderRadius: Float
        get() = roundRadius
        set(value) { roundRadius = value }

    var shadowRadius = 0f
        set(value) {
            val px = paddingX
            val py = paddingY
            field = value
            paddingX = px
            paddingY = py
        }

    var color: Int
        get() = fillColor
        set(value) {
            fillColor = value
            invalidate()
        }

    var splitterWidth = 0f
    var splitterHeight = 0f

    var splitterColor = 0
    
    init {
        // TODO: fix conflict with padding
        // setLayerType(LAYER_TYPE_SOFTWARE, null)

        val style = if (defStyleAttr == 0) R.attr.roundIndicatorStyle else defStyleAttr
        val a = context.obtainStyledAttributes(attrs, R.styleable.XHBRoundIndicator, style, 0)
        mode = a.getInt(R.styleable.XHBRoundIndicator_widthMode, mode)
        paddingX = a.getDimensionPixelSize(R.styleable.XHBRoundIndicator_paddingX, paddingX)
        paddingY = a.getDimensionPixelSize(R.styleable.XHBRoundIndicator_paddingY, paddingY)
        roundRadius = a.getDimension(R.styleable.XHBRoundIndicator_borderRadius, roundRadius)
        shadowRadius = a.getDimension(R.styleable.XHBRoundIndicator_shadowRadius, shadowRadius)
        color = a.getColor(R.styleable.XHBRoundIndicator_color, color)
        splitterWidth = context.resources.getDimension(R.dimen.xhb_round_indicator_splitter_width)
        splitterHeight = a.getDimension(R.styleable.XHBRoundIndicator_splitterHeight, splitterHeight)
        splitterColor = a.getColor(R.styleable.XHBRoundIndicator_splitterColor, splitterColor)
        a.recycle()
    }

    private var splitterPositions: List<Float> = listOf()
    private val splitterRect = RectF()

    override fun onDraw(canvas: Canvas) {
        if (splitterHeight > 0) {
            paint.setShadowLayer(0f, 0f, 0f, 0)
            paint.color = splitterColor
            val dw = splitterWidth / 2
            for (p in splitterPositions) {
                splitterRect.left = p - dw
                splitterRect.right = p + dw
                canvas.drawRect(splitterRect, paint)
            }
        }
        paint.setShadowLayer(shadowRadius, 0f, 0f, Color.LTGRAY)
        super.onDraw(canvas)
    }

    override fun onPositionDataProvide(dataList: MutableList<PositionData>) {
        super.onPositionDataProvide(dataList)
        if (dataList.size > 0) {
            splitterPositions = dataList.take(dataList.size - 1).map { it.mRight.toFloat() }
            val c = (dataList[0].mContentTop + dataList[0].mContentBottom) / 2f
            val dh = splitterHeight / 2
            splitterRect.top = c - dh
            splitterRect.bottom = c + dh
        }
    }

}
