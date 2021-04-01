package com.xhb.uibase.demo.components.simple

import android.content.Context
import android.os.Bundle
import android.view.View
import com.xhb.uibase.demo.core.ComponentFragment
import com.xhb.uibase.demo.core.SkinManager
import com.xhb.uibase.demo.core.ViewModel
import com.xhb.uibase.demo.core.ViewStyles
import com.xhb.uibase.demo.databinding.XhbListFragmentBinding
import com.xhb.uibase.dialog.*
import kotlinx.android.synthetic.main.xhb_dialog_fragment.*
import skin.support.observe.SkinObservable
import skin.support.observe.SkinObserver

class XHBListFragment : ComponentFragment<XhbListFragmentBinding?, XHBListFragment.Model?, XHBListFragment.Styles?>(), SkinObserver {

    class Model(fragment: XHBListFragment?) : ViewModel()

    class Styles(fragment: XHBListFragment?) : ViewStyles()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SkinManager.addObserver(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { initView(it) }
    }

    override fun onDestroy() {
        SkinManager.removeObserver(this)
        super.onDestroy()
    }

    override fun updateSkin(observable: SkinObservable, o: Any) {
    }

    private fun initView(context: Context) {

    }

    companion object {
        private const val TAG = "XHBListFragment"
    }
}