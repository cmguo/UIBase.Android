package com.eazy.uibase.widget

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.Gravity
import androidx.viewpager.widget.ViewPager
import com.eazy.uibase.R
import com.eazy.uibase.widget.banner.Banner
import com.eazy.uibase.widget.banner.transformer.*

class ZCarouselView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.carouselViewStyle
) : Banner(context, attrs, defStyleAttr) {

    interface OnSlideIndexChangeListener {
        fun onSlideIndexChanged(view: ZCarouselView, index: Int)
    }

    enum class SlideDirection {
        LeftToRight,
        RightToLeft,
        TopToBottom,
        BottomToTop
    }

    enum class AnimationMode(val transformer: Class<out ViewPager.PageTransformer>) {
        None(DefaultTransformer::class.java),
        Accordion(AccordionTransformer::class.java),
        BackgroundToForeground(BackgroundToForegroundTransformer::class.java),
        ForegroundToBackground(ForegroundToBackgroundTransformer::class.java),
        CubeIn(CubeInTransformer::class.java),
        CubeOut(CubeOutTransformer::class.java),
        DepthPage(DepthPageTransformer::class.java),
        FlipHorizontal(FlipHorizontalTransformer::class.java),
        FlipVertical(FlipVerticalTransformer::class.java),
        RotateDown(RotateDownTransformer::class.java),
        RotateUp(RotateUpTransformer::class.java),
        ScaleInOut(ScaleInOutTransformer::class.java),
        Stack(StackTransformer::class.java),
        Tablet(TabletTransformer::class.java),
        ZoomIn(ZoomInTransformer::class.java),
        ZoomOut(ZoomOutTransformer::class.java),
        ZoomOutSlide(ZoomOutSlideTransformer::class.java)
    }

    enum class IndicatorType {
        None,
        Circle,
        Number,
        NumberTitle,
        CircleTitle,
        CircleTitleInside
    }

    var titles: Array<Any> = arrayOf()
        set(value) {
            field = value
            setTitles(value.map { it.toString() })
        }

    var images: Array<Any> = arrayOf()
        set(value) {
            field = value
            setImages(value.asList())
        }

    var slideDirection = SlideDirection.LeftToRight
        set(value) {
            field = value
        }

    var animationMode: AnimationMode = AnimationMode.None
        set(value) {
            field = value
            setBannerAnimation(value.transformer)
        }

    var slideInterval: Int = 0
        set(value) {
            setDelayTime(value)
            isAutoPlay(value > 0)
        }

    var manualSlidable: Boolean = false
        set(value) {
            setViewPagerIsScroll(value)
        }

    var slideIndex: Int
        get() = currentItem
        set(value) {
            currentItem = value
        }

    var indicatorType = IndicatorType.None
        set(value) {
            field = value
            setIndicatorType(value.ordinal);
        }

    var indicatorGravity = Gravity.NO_GRAVITY
        set(value) {
            field = value
            setIndicatorGravity(value)
        }

    private var _listener: OnSlideIndexChangeListener? = null

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ZCarouselView, defStyleAttr, 0)

        a.recycle()

        setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
            override fun onPageSelected(position: Int) {
                _listener?.onSlideIndexChanged(this@ZCarouselView, position)
            }
        })
    }

    fun setOnSlideIndexChangeListener(listener: OnSlideIndexChangeListener?) {
        _listener = listener
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }

    companion object {
        private const val TAG = "ZCarouselView"
    }


}
