package com.xhb.uibase.demo.layouts;

import android.app.Dialog;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.google.auto.service.AutoService;
import com.xhb.uibase.binding.RecyclerViewAdapter;
import com.xhb.uibase.demo.R;
import com.xhb.uibase.demo.core.annotation.Author;
import com.xhb.uibase.demo.core.Component;
import com.xhb.uibase.demo.core.ComponentFragment;
import com.xhb.uibase.demo.core.ViewModel;
import com.xhb.uibase.demo.core.ViewStyles;
import com.xhb.uibase.demo.databinding.DialogsBinding;
import com.xhb.uibase.demo.view.recycler.PaddingDecoration;

import java.util.Map;

@AutoService(Component.class)
@Author("cmguo")
public class DialogsComponent implements Component
{
    @Override
    public int id() {
        return R.id.component_dialogs;
    }

    @Override
    public int group() {
        return R.string.group_styles;
    }

    @Override
    public int icon() {
        return android.R.drawable.btn_plus;
    }

    @Override
    public int title() {
        return R.string.component_dialogs;
    }

    @Override
    public int description() {
        return R.string.component_dialogs_desc;
    }

    @Override
    public Class<? extends ComponentFragment> fragmentClass() {
        return DialogsFragment.class;
    }
}

