package com.eazy.uibase.demo.components.simple

import android.content.Context
import android.os.Bundle
import android.view.View
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.SkinManager
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.databinding.ListFragmentBinding
import com.eazy.uibase.dialog.*
import kotlinx.android.synthetic.main.dialog_fragment.*
import skin.support.observe.SkinObservable
import skin.support.observe.SkinObserver

class ZListFragment : ComponentFragment<ListFragmentBinding?, ZListFragment.Model?, ZListFragment.Styles?>(), SkinObserver {

    class Model(fragment: ZListFragment?) : ViewModel()

    class Styles(fragment: ZListFragment?) : ViewStyles()

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
        private const val TAG = "ZListFragment"
    }
}