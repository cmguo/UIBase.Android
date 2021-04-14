package com.eazy.uibase.demo.components.simple

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.Bindable
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.databinding.ActivityViewFragmentBinding
import com.eazy.uibase.view.list.RecyclerViewAdapter.OnItemClickListener
import com.eazy.uibase.widget.ZPanel
import com.eazy.uibase.widget.ZTipView

class ZActivityViewFragment : ComponentFragment<ActivityViewFragmentBinding?, ZActivityViewFragment.Model?, ZActivityViewFragment.Styles?>()
    , ZPanel.PanelListener, OnItemClickListener<Int> {

    class Model : ViewModel() {

        private val list1 = listOf(
            R.array.share_class,
            R.array.share_weixin,
            R.array.share_moment,
            R.array.share_qq,
            R.array.share_space,
            R.array.share_more,
        )

        private val list2 = listOf(
            R.array.op_stick,
            R.array.op_copy,
            R.array.op_repost,
            R.array.op_recall,
            R.array.op_star,
            R.array.op_weblink,
            R.array.op_refresh,
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
        private const val TAG = "ZActivityViewFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.activityViews.root.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bluegrey_00))
        return view
    }

    override fun backgroundColor(): Int {
        return R.color.blue_100
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.activityViews.root.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bluegrey_00))
    }

    private var lp : ViewGroup.LayoutParams? = null

    var buttonClick = View.OnClickListener {
        val panel = ZPanel(requireContext())
        panel.titleBar = R.style.title_bar_text_only
        panel.bottomButton = R.string.cancel
        panel.listener = this
        binding.frame.removeView(binding.activityViews.root)
        lp = binding.activityViews.root.layoutParams
        panel.addView(binding.activityViews.root)
        panel.popUp(parentFragmentManager)
    }

    override fun panelDismissed(panel: ZPanel) {
        panel.removeView(binding.activityViews.root)
        binding.frame.addView(binding.activityViews.root, lp)
    }

    override fun onItemClick(position: Int, `object`: Int?) {
       val tip = ZTipView(requireContext())
        tip.message="点击了按钮${position}"
        tip.location = ZTipView.Location.AutoToast
        tip.popAt(requireView())
    }

}