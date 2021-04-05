package com.xhb.uibase.daynight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatViewInflater;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatToggleButton;

import java.lang.reflect.Method;

public class DayNightBaseViewInflater extends AppCompatViewInflater {

    protected void inspectView(View view, AttributeSet attrs) {
    }

    @NonNull
    @Override
    protected AppCompatTextView createTextView(Context context, AttributeSet attrs) {
        return onViewCreated(super.createTextView(context, attrs), attrs);
    }

    @NonNull
    @Override
    protected AppCompatImageView createImageView(Context context, AttributeSet attrs) {
        return onViewCreated(super.createImageView(context, attrs), attrs);
    }

    @NonNull
    @Override
    protected AppCompatButton createButton(Context context, AttributeSet attrs) {
        return onViewCreated(super.createButton(context, attrs), attrs);
    }

    @NonNull
    @Override
    protected AppCompatEditText createEditText(Context context, AttributeSet attrs) {
        return onViewCreated(super.createEditText(context, attrs), attrs);
    }

    @NonNull
    @Override
    protected AppCompatSpinner createSpinner(Context context, AttributeSet attrs) {
        return onViewCreated(super.createSpinner(context, attrs), attrs);
    }

    @NonNull
    @Override
    protected AppCompatImageButton createImageButton(Context context, AttributeSet attrs) {
        return onViewCreated(super.createImageButton(context, attrs), attrs);
    }

    @NonNull
    @Override
    protected AppCompatCheckBox createCheckBox(Context context, AttributeSet attrs) {
        return onViewCreated(super.createCheckBox(context, attrs), attrs);
    }

    @NonNull
    @Override
    protected AppCompatRadioButton createRadioButton(Context context, AttributeSet attrs) {
        return onViewCreated(super.createRadioButton(context, attrs), attrs);
    }

    @NonNull
    @Override
    protected AppCompatCheckedTextView createCheckedTextView(Context context, AttributeSet attrs) {
        return onViewCreated(super.createCheckedTextView(context, attrs), attrs);
    }

    @NonNull
    @Override
    protected AppCompatAutoCompleteTextView createAutoCompleteTextView(Context context, AttributeSet attrs) {
        return onViewCreated(super.createAutoCompleteTextView(context, attrs), attrs);
    }

    @NonNull
    @Override
    protected AppCompatMultiAutoCompleteTextView createMultiAutoCompleteTextView(Context context, AttributeSet attrs) {
        return onViewCreated(super.createMultiAutoCompleteTextView(context, attrs), attrs);
    }

    @NonNull
    @Override
    protected AppCompatRatingBar createRatingBar(Context context, AttributeSet attrs) {
        return onViewCreated(super.createRatingBar(context, attrs), attrs);
    }

    @NonNull
    @Override
    protected AppCompatSeekBar createSeekBar(Context context, AttributeSet attrs) {
        return onViewCreated(super.createSeekBar(context, attrs), attrs);
    }

    @NonNull
    @Override
    protected AppCompatToggleButton createToggleButton(Context context, AttributeSet attrs) {
        return onViewCreated(super.createToggleButton(context, attrs), attrs);
    }

    private static Method createViewFromTag = null;
    static {
        try {
            createViewFromTag = AppCompatViewInflater.class.getDeclaredMethod(
                "createViewFromTag", Context.class, String.class, AttributeSet.class);
            createViewFromTag.setAccessible(true);
        } catch (NoSuchMethodException ignored) {
        }
    }

    @Nullable
    @Override
    protected View createView(Context context, String name, AttributeSet attrs) {
        if (createViewFromTag == null)
            return null;
        try {
            View view = (View) createViewFromTag.invoke(this, context, name, attrs);
            if (view != null)
                return onViewCreated(view, attrs);
            else
                return null;
        } catch (Throwable e) {
            return null;
        }
    }

    private <T extends View> T onViewCreated(T view, AttributeSet attrs) {
        inspectView(view, attrs);
        return view;
    }

}
