package com.eazy.uibase.utils;

import android.app.Activity;
import android.content.Context;
import android.text.InputFilter;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;


public class CommonUtil {

    public static int getColor(Context context, int colorId) {
        return ContextCompat.getColor(context, colorId);
    }

    public static String getString(Context context, @StringRes int resId) {
        return context.getString(resId);
    }

    public static String getString(Context context, @StringRes int resId, Object... objects) {
        return context.getString(resId, objects);
    }

    /**
     * 设置 EditText 输入的最大字长
     *
     * @param mEditText
     * @param maxLength
     */
    public static void setMaxLengthForEditText(EditText mEditText, int maxLength) {
        InputFilter[] filters = {new InputFilter.LengthFilter(maxLength)};
        mEditText.setFilters(filters);
    }

    public static void hideKeyBoard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        final View currentFocus = context.getCurrentFocus();
        if (imm != null && imm.isActive() && currentFocus != null) {
            if (currentFocus.getWindowToken() != null) {
                imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public static void hideKeyBoard(@Nullable Context context, @Nullable EditText editText) {
        if (editText == null || context == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public static void showKeyBoard(Context context, EditText editText) {
        if (editText == null) {
            return;
        }
        editText.requestFocus();
        final InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 隐藏键盘
     *
     * @param inputMethodManager
     * @param view
     */
    public static void hideInputMethodManager(InputMethodManager inputMethodManager, View view) {
        if (inputMethodManager != null && inputMethodManager.isActive() && view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 根据EditText的信息将光标置为最后一个字符后面
     *
     * @param editText
     */
    public static void setLastSelectionOfEditText(EditText editText) {
        if (editText != null) {
            editText.setSelection(editText.getText().toString().length());
        }
    }

    /**
     * 切换EditText 密文/明文
     *
     * @param editText
     * @param isPasswordTransformation
     */
    public static void setPasswordTransformationToEditText(EditText editText, Boolean isPasswordTransformation) {
        if (editText != null) {
            if (isPasswordTransformation) {
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            CommonUtil.setLastSelectionOfEditText(editText);
        }
    }
}
