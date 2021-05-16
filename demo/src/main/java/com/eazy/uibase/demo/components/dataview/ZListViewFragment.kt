package com.eazy.uibase.demo.components.dataview

import androidx.databinding.Bindable
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.annotation.Title
import com.eazy.uibase.demo.databinding.ListViewFragmentBinding
import com.eazy.uibase.widget.ZListItemView

class ZListViewFragment : ComponentFragment<ListViewFragmentBinding?, ZListViewFragment.Model?, ZListViewFragment.Styles?>() {

    class Model(fragment: ZListViewFragment?) : ViewModel() {
        val colors = arrayListOf<ZListItemView.Data>()
        val colorGroups = arrayListOf<ZListItemView.GroupData>()
    }

    class Styles : ViewStyles() {

        @Bindable
        @Title("分组")
        val group = false
    }

    companion object {
        private const val TAG = "ZListFragment"
    }
}
