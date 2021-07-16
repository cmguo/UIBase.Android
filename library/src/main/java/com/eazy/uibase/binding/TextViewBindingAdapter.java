package com.eazy.uibase.binding;

import android.widget.TextView;

import androidx.annotation.StyleRes;
import androidx.databinding.BindingAdapter;

import com.eazy.uibase.view.TextViewKt;

public class TextViewBindingAdapter {

    @BindingAdapter("textAppearance")
    public static void setTextAppearance(TextView view, @StyleRes int idRes) {
        TextViewKt.setTextAppearance(view, idRes);
    }

}
