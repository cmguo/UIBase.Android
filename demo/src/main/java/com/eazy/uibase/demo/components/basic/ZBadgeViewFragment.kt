package com.eazy.uibase.demo.components.basic

import android.os.Bundle
import android.util.SizeF
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Bindable
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.BR
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.DimenStyle
import com.eazy.uibase.demo.core.style.GravityStyle
import com.eazy.uibase.demo.core.style.annotation.*
import com.eazy.uibase.demo.databinding.BadgeViewFragmentBinding
import com.eazy.uibase.demo.databinding.BadgeViewItemBinding
import com.eazy.uibase.widget.ZBadgeView

class ZBadgeViewFragment : ComponentFragment<BadgeViewFragmentBinding?,
    ZBadgeViewFragment.Model?, ZBadgeViewFragment.Styles?>() {

    class Model : ViewModel()

    class Styles : ViewStyles() {

        @Bindable
        @Title("边框宽度")
        @Description("加上边框；如果徽标显示在复杂图形上或者带有强烈色彩的图标上，建议具有1px容器背景色（通常为白色）的描边。")
        @Style(DimenStyle::class)
        var borderWidth = 0.5f

        @Bindable
        @Title("对齐方式")
        @Description("对齐到哪个角落，9个点；可结合位置偏移来精确定位；默认右上角")
        @Style(GravityStyle::class)
        var gravity = Gravity.RIGHT or Gravity.TOP

        @Bindable
        @Title("位置偏移")
        @Description("相对于对齐点的偏移量，向中心偏移；如果某一边是中间对齐，则这边无法再调整")
        @Style(DimenStyle::class)
        var offset = SizeF(0f, 0f)

        @Bindable
        @Title("拖拽删除")
        @Description("通过拖拽小圆点，可以将其删除，拖拽过程中会有事件回调")
        var draggabe = false

        @Bindable
        @Title("拖拽距离")
        @Description("最大拖拽半径，当超过该范围时松开，可以将其删除，拖拽过程中会有事件回调")
        @Style(DimenStyle::class)
        var dragRadius = 90

        @Bindable
        @Title("最大数值")
        @Description("如果数值超过最大数值，展示为XX+，XX为最大数值")
        var maximum = 0

        @Bindable
        @Title("数字")
        @Description("改变数字，实际作用是改变文字；但是在非精确模式下，会作调整")
        var number = 1

        @Bindable
        @Title("文字")
        @Description("改变文字，小圆点会自动适应文字宽度")
        var text = "1"

        @Bindable @Ignore
        var dragState: ZBadgeView.DragState? = null
            set(value) {
                field = value
                notifyPropertyChanged(BR.dragState)
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        addBadge(inflater, binding.textview)
        addBadge(inflater, binding.imageview)
        addBadge(inflater, binding.button)
        return view
    }

    private fun addBadge(inflater: LayoutInflater, target: View) {
        val binding = BadgeViewItemBinding.inflate(inflater)
        binding.styles = styles
        (binding.root as ZBadgeView).bindTarget(target)
    }

    companion object {
        private const val TAG = "ZBadgeViewFragment2"
    }
}