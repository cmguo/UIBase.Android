package com.eazy.uibase.demo.view.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eazy.uibase.demo.core.Component;
import com.eazy.uibase.demo.core.ComponentFragment;
import com.eazy.uibase.demo.core.ComponentInfo;
import com.eazy.uibase.demo.core.ComponentStyle;
import com.eazy.uibase.demo.core.ComponentStyles;
import com.eazy.uibase.demo.core.ViewStyles;

import java.util.ArrayList;
import java.util.List;

public class InformationViewModel extends ViewModel {

    public MutableLiveData<ComponentInfo> componentInfo = new MutableLiveData<>();

    public void bindComponent(ComponentFragment fragment) {
        componentInfo.postValue(new ComponentInfo(fragment.getContext(), fragment.getComponent()));
    }
}
