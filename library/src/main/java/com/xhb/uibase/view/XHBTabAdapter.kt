package com.xhb.uibase.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.xhb.uibase.view.list.ItemBinding
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView

class XHBTabAdapter<T>(val titles: List<T>, val itemBinding: ItemBinding<T>, private val indicator: IPagerIndicator)
    : CommonNavigatorAdapter(), View.OnClickListener {

    @FunctionalInterface
    interface TitleSelectListener<T> {
        fun titleSelected(index: Int, title: T)
    }

    var listener: TitleSelectListener<T>? = null

    fun bindViewPager(pager: ViewPager) {
        listener = object : TitleSelectListener<T> {
            override fun titleSelected(index: Int, title: T) {
                pager.currentItem = index
            }
        }
    }

    override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
        val binding = itemBinding.createBinding(null, itemBinding.getItemViewType(titles[index]))
        itemBinding.bindView(binding, titles[index], index)
        binding.root.setOnClickListener(this)
        return binding.root as IPagerTitleView
    }

    override fun getCount(): Int = titles.size

    override fun getIndicator(context: Context?): IPagerIndicator {
        return indicator
    }

    override fun onClick(v: View) {
        val index = (v.parent as ViewGroup).indexOfChild(v)
        val data = titles[index]
        listener?.titleSelected(index, data)
    }
}
