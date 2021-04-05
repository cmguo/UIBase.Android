package com.xhb.uibase.demo.components.simple

import android.content.Context
import android.os.Bundle
import android.view.View
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.ViewModel
import com.xhb.uibase.demo.core.ViewStyles
import com.xhb.uibase.demo.databinding.XhbListFragmentBinding
import com.xhb.uibase.dialog.*
import kotlinx.android.synthetic.main.xhb_dialog_fragment.*

class XHBListFragment : ComponentFragment<XhbListFragmentBinding?, XHBListFragment.Model?, XHBListFragment.Styles?>() {

    class Model(fragment: XHBListFragment?) : ViewModel()

    class Styles(fragment: XHBListFragment?) : ViewStyles()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { initView(it) }
    }

    private fun initView(context: Context) {
    }

    companion object {
        private const val TAG = "XHBListFragment"
    }
}