package com.eazy.uibase.demo.view.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eazy.uibase.demo.core.ComponentFragment;
import com.eazy.uibase.demo.core.ComponentInfo;

public class InformationViewModel extends ViewModel {

    public MutableLiveData<ComponentInfo> componentInfo = new MutableLiveData<>();

    public void bindComponent(ComponentFragment fragment) {
        componentInfo.postValue(new ComponentInfo(fragment.getContext(), fragment.getComponent()));
    }
}
