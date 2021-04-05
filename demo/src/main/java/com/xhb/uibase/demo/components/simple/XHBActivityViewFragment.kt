package com.xhb.uibase.demo.components.simple

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Bindable
import com.xhb.uibase.demo.BR
import com.xhb.uibase.demo.R
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.ViewModel
import com.xhb.uibase.demo.core.ViewStyles
import com.xhb.uibase.demo.databinding.XhbActivityViewFragmentBinding
import com.xhb.uibase.view.list.RecyclerViewAdapter.OnItemClickListener
import com.xhb.uibase.widget.XHBPanel
import com.xhb.uibase.widget.XHBTipView
import kotlinx.android.synthetic.main.xhb_activity_view_fragment.view.*
import kotlinx.android.synthetic.main.xhb_number_view_fragment.view.*

class XHBActivityViewFragment : ComponentFragment<XhbActivityViewFragmentBinding?, XHBActivityViewFragment.Model?, XHBActivityViewFragment.Styles?>()
    , XHBPanel.PanelListener, OnItemClickListener<Int> {

    class Model : ViewModel() {

        private val list1 = listOf(
            R.array.xhb_share_class,
            R.array.xhb_share_weixin,
            R.array.xhb_share_moment,
            R.array.xhb_share_qq,
            R.array.xhb_share_space,
            R.array.xhb_share_more,
        )

        private val list2 = listOf(
            R.array.xhb_op_stick,
            R.array.xhb_op_copy,
            R.array.xhb_op_repost,
            R.array.xhb_op_recall,
            R.array.xhb_op_star,
            R.array.xhb_op_weblink,
            R.array.xhb_op_refresh,
        )

        @Bindable
        val lists: Array<List<Int>> = arrayOf(list1, list2)
    }

    class Styles : ViewStyles() {

        @Bindable
        var content = 0
            set(value) {
                field = value
            }
    }

    companion object {
        private const val TAG = "XHBActivityViewFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        //binding.activityViews.root.setBackgroundColor(Color.WHITE)
        return view
    }

    override fun backgroundColor(): Int {
        return R.color.blue_100
    }

    private var lp : ViewGroup.LayoutParams? = null

    var buttonClick = View.OnClickListener {
        val panel = XHBPanel(requireContext())
        panel.titleBar = R.style.title_bar_text
        panel.bottomButton = R.string.cancel
        panel.listener = this
        binding.frame.removeView(binding.activityViews.root)
        lp = binding.activityViews.root.layoutParams
        panel.addView(binding.activityViews.root)
        panel.popUp(parentFragmentManager)
    }

    override fun panelDismissed(panel: XHBPanel) {
        panel.removeView(binding.activityViews.root)
        binding.frame.addView(binding.activityViews.root, lp)
    }

    override fun onItemClick(position: Int, `object`: Int?) {
       val tip = XHBTipView(requireContext())
        tip.message="点击了按钮${position}"
        tip.location = XHBTipView.Location.AutoToast
        tip.popAt(requireView())
    }

}