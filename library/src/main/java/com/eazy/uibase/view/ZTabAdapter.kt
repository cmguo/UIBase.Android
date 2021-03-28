package com.eazy.uibase.view

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.viewpager.widget.ViewPager
import com.eazy.uibase.view.list.ItemBinding
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView

class ZTabAdapter<T>(titles: Iterator<T>, val itemBinding: ItemBinding<T>, private val indicator: IPagerIndicator)
    : CommonNavigatorAdapter(), View.OnClickListener {

    var titles = mutableListOf<T>()

    init {
        for (t in titles)
            this.titles.add(t)
    }

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

    override fun getTitleView(parent: ViewGroup?, index: Int): IPagerTitleView {
        val binding = itemBinding.createBinding(parent, itemBinding.getItemViewType(titles[index]))
        itemBinding.bindView(binding, titles[index], index)
        if (!(binding is ViewDataBinding)) {
            (binding.root as? TextView)?.text = titles[index].toString()
        }
        binding.root.setOnClickListener(this)
        return binding.root as IPagerTitleView
    }

    override fun getCount(): Int = titles.size

    override fun getIndicator(parent: ViewGroup?): IPagerIndicator {
        return indicator
    }

    override fun onClick(v: View) {
        val index = (v.parent as ViewGroup).indexOfChild(v)
        val data = titles[index]
        listener?.titleSelected(index, data)
    }
}
