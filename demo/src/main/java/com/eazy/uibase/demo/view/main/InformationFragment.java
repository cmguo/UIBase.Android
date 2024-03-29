package com.eazy.uibase.demo.view.main;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.ComponentFragment;
import com.eazy.uibase.demo.databinding.InformationFragmentBinding;

import org.jetbrains.annotations.NotNull;

public class InformationFragment extends Fragment {

    private InformationViewModel mViewModel;
    private ComponentFragment<?,?,?> fragment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        InformationFragmentBinding binding = InformationFragmentBinding.inflate(inflater);
        binding.setLifecycleOwner(this);
        binding.setFragment(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(InformationViewModel.class);
        if (fragment != null)
            mViewModel.bindComponent(fragment);
        DataBindingUtil.findBinding(view).setVariable(BR.model, mViewModel);
    }

    public void bindComponent(ComponentFragment<?,?,?> fragment) {
        if (mViewModel != null)
            mViewModel.bindComponent(fragment);
        else
            this.fragment = fragment;
    }
}