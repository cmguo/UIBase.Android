package com.eazy.uibase.demo.components.input

import androidx.databinding.Bindable
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.style.annotation.Description
import com.eazy.uibase.demo.core.style.annotation.Title
import com.eazy.uibase.demo.databinding.NumberViewFragmentBinding

class ZNumberViewFragment : ComponentFragment<NumberViewFragmentBinding?, ZNumberViewFragment.Model?, ZNumberViewFragment.Styles?>() {

    class Model : ViewModel() {
    }

    class Styles : ViewStyles() {

        @Bindable
        @Title("最小数量") @Description("最小输入数量；默认0")
        var minimun = 0

        @Bindable
        @Title("最大数量") @Description("最大输入数量；设置0，则不限制数量；默认为0")
        var maximun = 0

        @Bindable
        @Title("步进距离") @Description("每次点击增减的数量")
        var step = 1

        @Bindable
        @Title("数量") @Description("当前输入的数量")
        var number = 1
    }

    companion object {
        private const val TAG = "ZAmountViewFragment"
    }
}