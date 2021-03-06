package com.eazy.uibase.demo.components.basic

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Bindable
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.eazy.uibase.binding.RecyclerViewAdapter.OnItemClickListener
import com.eazy.uibase.binding.RecyclerViewAdapter.UnitTypeItemLayout
import com.eazy.uibase.demo.R
import com.eazy.uibase.demo.core.ComponentFragment
import com.eazy.uibase.demo.core.SkinManager
import com.eazy.uibase.demo.core.ViewModel
import com.eazy.uibase.demo.core.ViewStyles
import com.eazy.uibase.demo.core.annotation.Description
import com.eazy.uibase.demo.core.annotation.Title
import com.eazy.uibase.demo.databinding.ButtonFragmentBinding
import com.eazy.uibase.demo.databinding.ButtonItemBinding
import com.eazy.uibase.demo.view.recycler.PaddingDecoration
import com.eazy.uibase.widget.ZButton
import skin.support.observe.SkinObservable
import skin.support.observe.SkinObserver

class ZButtonFragment : ComponentFragment<ButtonFragmentBinding?,
    ZButtonFragment.Model?, ZButtonFragment.Style?>(), SkinObserver {

    class Model(fragment: ZButtonFragment) : ViewModel() {
        val types = ZButton.ButtonType.values().asList()
    }

    class Style(private val fragment_: ZButtonFragment) : ViewStyles() {
        enum class ButtonWidth(private val layoutWidth_: Int) {
            WrapContent(ViewGroup.LayoutParams.WRAP_CONTENT), MatchParent(ViewGroup.LayoutParams.MATCH_PARENT);
            fun layoutWidth(): Int {
                return layoutWidth_
            }
        }

        var itemLayout = ButtonItemLayout(this)
        var itemDecoration: ItemDecoration = PaddingDecoration()

        @Bindable
        @Title("禁用")
        @Description("切换到禁用状态")
        @JvmField var disabled = false

        @Bindable
        @Title("加载")
        @Description("切换到加载状态")
        @JvmField var loading = false

        @Bindable
        @Title("尺寸模式")
        @Description("有下列尺寸模式：大（Large）、中（Middle）、小（Small），默认：Large")
        var sizeMode = ZButton.ButtonSize.Large

        @Bindable
        @Title("宽度模式")
        @Description("有下列宽度模式：适应内容（WrapContent）、适应布局（MatchParent），默认：WrapContent")
        var widthMode = ButtonWidth.WrapContent

        @Bindable
        @Title("文字")
        @Description("改变文字，按钮会自动适应文字宽度")
        var text = "按钮"

        fun testButtonClick(view: View) {
            if (view is ZButton) {
                val loadingView = view as ZButton
                if (!loadingView.loading) {
                    view.postDelayed({ testButtonClick(view) }, 3000)
                }
                loadingView.loading = !loadingView.loading
            }
        }

        override fun notifyPropertyChanged(fieldId: Int) {
            if (fieldId == BR.widthMode) {
                fragment_.updateButtons()
                return
            }
            super.notifyPropertyChanged(fieldId)
        }
    }

    class ButtonItemLayout(private val style: Style) : UnitTypeItemLayout<ZButton.ButtonType>(R.layout.button_item) {
        override fun bindView(binding: ViewDataBinding?, item: ZButton.ButtonType, position: Int) {
            super.bindView(binding, item, position)
            binding!!.setVariable(BR.style, style)
            val button = (binding as ButtonItemBinding)!!.button
            val lp = button.layoutParams
            lp.width = style.widthMode.layoutWidth()
            button.layoutParams = lp
        }
    }

    // this should be in view model, but fragment may simplify things
    val buttonClicked = OnItemClickListener<Any> { position, `object` ->
        Log.d(TAG, "buttonClicked$`object`")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SkinManager.addObserver(this)
    }

    override fun onDestroy() {
        SkinManager.removeObserver(this)
        super.onDestroy()
    }

    override fun updateSkin(observable: SkinObservable, o: Any) {
        updateButtons()
    }

    private fun updateButtons() {
        binding!!.buttonList.adapter!!.notifyItemRangeChanged(0, model!!.types.size)
    }

    companion object {
        private const val TAG = "ZButtonFragment2"
    }
}