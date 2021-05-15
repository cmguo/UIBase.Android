package com.eazy.uibase.demo.components.dataview

import android.view.Gravity
import androidx.databinding.Bindable
import com.eazy.uibase.demo.BR
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.DimenStyle
import com.eazy.uibase.demo.core.style.GravityStyle
import com.eazy.uibase.demo.core.style.annotation.Description
import com.eazy.uibase.demo.core.style.annotation.Style
import com.eazy.uibase.demo.core.style.annotation.Title
import com.eazy.uibase.demo.databinding.CarouselViewFragmentBinding
import com.eazy.uibase.demo.imageLoader.GlideImageLoader
import com.eazy.uibase.widget.ZCarouselView

class ZCarouselViewFragment : ComponentFragment<CarouselViewFragmentBinding?, ZCarouselViewFragment.Model?, ZCarouselViewFragment.Styles?>() {

    class Model : ViewModel() {

        val titles = arrayOf("标题一", "标题二", "标题三")

        val images = arrayOf(R.drawable.b1, R.drawable.b2, R.drawable.b3)

        val loader = GlideImageLoader()
    }

    class Styles : ViewStyles() {

        @Bindable
        @Title("滚动方向") @Description("默认横向滚动")
        var slideDirection = ZCarouselView.SlideDirection.LeftToRight

        @Bindable
        @Title("间隔时间") @Description("两次自动滚动的间隔时间，设为 0 不自动滚动")
        var slideInterval = 2000

        @Bindable
        @Title("允许滚动") @Description("是否允许手动滚动")
        var manualSlidable = true

        @Bindable
        @Title("图片间距") @Description("相邻两张图片之间的间距")
        @Style(DimenStyle::class)
        var itemSpacing = 0

        @Bindable
        @Title("循环滚动") @Description("是否循环滚动，好像有无限张图片")
        var cyclic = true

        @Bindable
        @Title("翻页动效") @Description("翻页的动画效果")
        var animationMode = ZCarouselView.AnimationMode.None

        @Bindable
        @Title("指示器") @Description("指示器类型")
        var indicatorType = ZCarouselView.IndicatorType.Circle

        @Bindable
        @Title("指示器位置") @Description("指示器位置")
        @Style(GravityStyle::class)
        var indicatorGravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL

        @Bindable
        @Title("当前图片") @Description("当前显示的图片序号，可通过 OnSlideIndexChangeListener 跟踪其变化")
        var slideIndex = 0
            set(value) {
                field = value
                notifyPropertyChanged(BR.slideIndex)
            }
    }

    companion object {
        private const val TAG = "ZCarouselFragment"
    }

}