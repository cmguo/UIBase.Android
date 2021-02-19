package com.xhb.uibase.demo.buttons;

import android.util.Log;

import androidx.databinding.Bindable;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.google.auto.service.AutoService;
import com.xhb.uibase.binding.RecyclerViewAdapter;
import com.xhb.uibase.demo.R;
import com.xhb.uibase.demo.core.annotation.Author;
import com.xhb.uibase.demo.core.Component;
import com.xhb.uibase.demo.core.ComponentFragment;
import com.xhb.uibase.demo.core.ViewModel;
import com.xhb.uibase.demo.core.ViewStyles;
import com.xhb.uibase.demo.databinding.ButtonsBinding;
import com.xhb.uibase.demo.view.recycler.PaddingDecoration;

import java.util.Map;

@AutoService(Component.class)
@Author("cmguo")
public class ButtonsComponent implements Component{
    @Override
    public int id() {
        return R.id.component_buttons;
    }

    @Override
    public int group() {
        return R.string.group_buttons;
    }

    @Override
    public int icon() {
        return android.R.drawable.btn_plus;
    }

    @Override
    public int title() {
        return R.string.component_buttons;
    }

    @Override
    public int description() {
        return R.string.component_buttons_desc;
    }

    @Override
    public Class<? extends ComponentFragment> fragmentClass() {
        return ButtonsFragment.class;
    }

}

