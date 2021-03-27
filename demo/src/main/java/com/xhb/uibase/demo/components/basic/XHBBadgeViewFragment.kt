package com.xhb.uibase.demo.components.basic

import android.os.Bundle
import android.util.SizeF
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Bindable
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.SkinManager
import com.xhb.uibase.demo.core.ViewModel
import com.xhb.uibase.demo.BR
import com.xhb.uibase.demo.core.ViewStyles
import com.xhb.uibase.demo.core.style.DimemDpStyle
import com.xhb.uibase.demo.core.style.annotation.*
import com.xhb.uibase.demo.databinding.XhbBadgeViewFragmentBinding
import com.xhb.uibase.demo.databinding.XhbBadgeViewItemBinding
import com.xhb.uibase.widget.badgeview.XHBBadgeView
import skin.support.observe.SkinObservable
import skin.support.observe.SkinObserver

class XHBBadgeViewFragment : ComponentFragment<XhbBadgeViewFragmentBinding?,
    XHBBadgeViewFragment.Model?, XHBBadgeViewFragment.Styles?>(), SkinObserver {

    class Model : ViewModel() {
    }

    class Styles(private val fragment_: XHBBadgeViewFragment) : ViewStyles() {

        enum class ViewGravity(private val gravity_: Int) {
            LeftTop(Gravity.START or Gravity.TOP),
            CenterTop(Gravity.CENTER or Gravity.TOP),
            RigthTop(Gravity.END or Gravity.TOP),
            LeftCenter(Gravity.START or Gravity.CENTER),
            Center(Gravity.CENTER),
            RigthCenter(Gravity.END or Gravity.CENTER),
            LeftBottom(Gravity.START or Gravity.BOTTOM),
            CenterBottom(Gravity.CENTER or Gravity.BOTTOM),
            RigthBottom(Gravity.END or Gravity.BOTTOM);
            fun gravity(): Int {
                return gravity_
            }
        }

        @Bindable
        @Title("边框宽度")
        @Description("加上边框；如果徽标显示在复杂图形上或者带有强烈色彩的图标上，建议具有1px容器背景色（通常为白色）的描边。")
        @Style(DimemDpStyle::class)
        var borderWidth = 0.5f

        @Bindable
        @Title("对齐方式")
        @Description("对齐到哪个角落，9个点；可结合位置偏移来精确定位；默认右上角")
        var gravity = ViewGravity.RigthTop

        @Bindable
        @Title("位置偏移")
        @Description("相对于对齐点的偏移量，向中心偏移；如果某一边是中间对齐，则这边无法再调整")
        @Style(DimemDpStyle::class)
        var offset = SizeF(0f, 0f)

        @Bindable
        @Title("拖拽删除")
        @Description("通过拖拽小圆点，可以将其删除，拖拽过程中会有事件回调")
        var draggabe = false

        @Bindable
        @Title("精确数值")
        @Description("如果不精确展示数值，超过99，展示为99+")
        var exactly = false

        @Bindable
        @Title("数字")
        @Description("改变数字，实际作用是改变文字；但是在非精确模式下，会作调整")
        var number = 1

        @Bindable
        @Title("文字")
        @Description("改变文字，小圆点会自动适应文字宽度")
        var text = "1"

        @Bindable @Ignore
        var dragState: XHBBadgeView.DragState? = null
            set(value) {
                field = value
                notifyPropertyChanged(BR.dragState)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SkinManager.addObserver(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        addBadge(inflater, binding!!.textview)
        addBadge(inflater, binding!!.imageview)
        addBadge(inflater, binding!!.button)
        return view
    }

    override fun onDestroy() {
        SkinManager.removeObserver(this)
        super.onDestroy()
    }

    override fun updateSkin(observable: SkinObservable, o: Any) {
    }

    fun addBadge(inflater: LayoutInflater, target: View) {
        val binding = XhbBadgeViewItemBinding.inflate(inflater);
        binding.styles = styles!!
        (binding.root as XHBBadgeView).bindTarget(target)
    }

    companion object {
        private const val TAG = "XHBBadgeViewFragment2"
    }
}