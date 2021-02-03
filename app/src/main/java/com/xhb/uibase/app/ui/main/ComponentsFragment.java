package com.xhb.uibase.app.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhb.uibase.app.MainActivity;
import com.xhb.uibase.app.R;
import com.xhb.uibase.demo.core.FragmentComponent;

import java.util.Observable;

public class ComponentsFragment extends Fragment {

    private ComponentsViewModel mViewModel;

    public static ComponentsFragment newInstance() {
        return new ComponentsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.components_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ComponentsViewModel.class);
        RecyclerView listView = (RecyclerView) getView().findViewById(R.id.componentsList);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setAdapter(new ComponentsAdapter(mViewModel.buildTree(getContext()), new ComponentsAdapter.OnItemClickListener<ComponentInfo>() {
            @Override
            public void onClick(RecyclerView recyclerView, View view, ComponentInfo data) {
                ((MainActivity) getActivity()).switchComponent(data.getComponent());
                ComponentsFragment.this.getView().setVisibility(View.INVISIBLE);
            }
        }, null));
    }

}