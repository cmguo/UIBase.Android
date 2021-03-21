package com.xhb.uibase.demo.view.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.xhb.uibase.demo.core.ComponentFragment;
import com.xhb.uibase.demo.core.ComponentInfo;

public class InformationViewModel extends ViewModel {

    public MutableLiveData<ComponentInfo> componentInfo = new MutableLiveData<>();

    public void bindComponent(ComponentFragment fragment) {
        componentInfo.postValue(fragment == null ? null : new ComponentInfo(fragment.getContext(), fragment.getComponent()));
    }
}
