package com.eazy.uibase.demo.view.main;

import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eazy.uibase.demo.BR;
import com.eazy.uibase.demo.R;
import com.eazy.uibase.demo.core.ComponentFragment;
import com.eazy.uibase.demo.core.ComponentStyle;
import com.eazy.uibase.demo.core.ComponentStyles;
import com.eazy.uibase.demo.core.ViewStyles;

import java.util.ArrayList;
import java.util.List;

public class StylesViewModel extends ViewModel {

    private static final String TAG = "StylesViewModel";

    public class StyleValue extends BaseObservable {

        ViewStyles styles;
        public ComponentStyle style;
        public int itemLayout = R.layout.style_value_list_item;
        private String value;

        @Bindable(value = "value")
        public boolean getChecked() {
            return "true".equals(value);
        }

        @Bindable
        public void setChecked(boolean value) {
            setValue(value ? "true" : "false");
        }

        @Bindable(value = "value")
        public int getSelection() {
            return style.getValues().indexOf(value);
        }

        @Bindable
        public void setSelection(int value) {
            setValue(style.getValues().get(value));
        }

        @Bindable
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            if (value == this.value)
                return;
            try {
                style.set(styles, value);
            } catch (Throwable e) {
                Log.w(TAG, "setValue", e);
                this.value = value;
            }
            String value2 = style.get(styles);
            if (!value2.equals(this.value)) {
                this.value = value2;
                notifyPropertyChanged(BR.value);
            }
        }
    }

    public MutableLiveData<List<StyleValue>> styleList = new MutableLiveData<>();

    public void bindComponent(ComponentFragment fragment) {
        ViewStyles styles = fragment.getStyles();
        ComponentStyles css = ComponentStyles.get(styles.getClass());
        List<StyleValue> list = new ArrayList<>();
        for (ComponentStyle cs : css.allStyles()) {
            StyleValue sv = new StyleValue();
            sv.styles = styles;
            sv.style = cs;
            sv.value = cs.get(styles);
            list.add(sv);
        }
        styleList.postValue(list);
    }
}