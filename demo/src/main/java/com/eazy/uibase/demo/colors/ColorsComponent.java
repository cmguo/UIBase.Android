package com.eazy.uibase.demo.colors;

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
import com.eazy.uibase.demo.databinding.ColorsBinding;
import com.eazy.uibase.demo.view.recycler.PaddingDecoration;

import java.util.Map;

@AutoService(Component.class)
@Author("cmguo")
public class ColorsComponent implements Component
{
    @Override
    public int id() {
        return R.id.component_colors;
    }

    @Override
    public int group() {
        return R.string.group_colors;
    }

    @Override
    public int icon() {
        return android.R.drawable.btn_star;
    }

    @Override
    public int title() {
        return R.string.component_colors;
    }

    @Override
    public int description() {
        return R.string.component_colors_desc;
    }

    @Override
    public Class<? extends ComponentFragment> fragmentClass() {
        return ColorsFragment.class;
    }
}

