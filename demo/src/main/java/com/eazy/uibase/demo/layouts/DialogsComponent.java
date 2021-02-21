package com.eazy.uibase.demo.layouts;

import android.app.Dialog;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.google.auto.service.AutoService;
import com.eazy.uibase.binding.RecyclerViewAdapter;
import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.annotation.Author;
import com.eazy.uibase.demo.core.Component;
import com.eazy.uibase.demo.core.ComponentFragment;
import com.eazy.uibase.demo.core.ViewModel;
import com.eazy.uibase.demo.core.ViewStyles;
import com.eazy.uibase.demo.databinding.DialogsBinding;
import com.eazy.uibase.demo.view.recycler.PaddingDecoration;

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

