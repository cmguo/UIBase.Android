package com.eazy.uibase.demo.components.simple

import android.content.Context
import android.os.Bundle
import android.view.View
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.databinding.CarouselFragmentBinding
import com.eazy.uibase.demo.imageLoader.GlideImageLoader
import kotlinx.android.synthetic.main.carousel_fragment.*
import java.util.*

class ZCarouselFragment : ComponentFragment<CarouselFragmentBinding?, ZCarouselFragment.Model?, ZCarouselFragment.Styles?>() {

    class Model(fragment: ZCarouselFragment?) : ViewModel()

    class Styles(fragment: ZCarouselFragment?) : ViewStyles()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { initView(it) }
    }

    private fun initView(context: Context) {
        val images = ArrayList<Int>()
        val titles = ArrayList<String>()
        images.add(R.drawable.b1)
        images.add(R.drawable.b2)
        images.add(R.drawable.b3)
        titles.add("标题一")
        titles.add("标题二")
        titles.add("标题三")
        //默认是CIRCLE_INDICATOR
        //默认是CIRCLE_INDICATOR
        banner.setImages(images)
            .setBannerTitles(titles)
            .setImageLoader(GlideImageLoader())
            .start()
    }

    companion object {
        private const val TAG = "ZCarouselFragment"
    }
}