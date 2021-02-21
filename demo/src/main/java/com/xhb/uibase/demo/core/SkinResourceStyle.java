package com.xhb.uibase.demo.core;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import com.airbnb.paris.styles.Style;
import com.airbnb.paris.typed_array_wrappers.TypedArrayWrapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import skin.support.content.res.SkinCompatResources;

class SkinResourceStyle implements Style
{
    private int styleRes;

    public SkinResourceStyle(int styleRes) {
        this.styleRes = styleRes;
    }

    @Override
    public boolean getShouldApplyDefaults() {
        return false;
    }

    @Override
    public boolean getShouldApplyParent() {
        return true;
    }

    @NotNull
    @Override
    public String name(@NotNull Context context) {
        return null;
    }

    @NotNull
    @Override
    public TypedArrayWrapper obtainStyledAttributes(@NotNull Context context, @NotNull int[] attrs) {
        return new SkinTypedArrayWrapper(context, context.obtainStyledAttributes(styleRes, attrs));
    }

    static class SkinTypedArrayWrapper extends TypedArrayWrapper {

        private Context context;
        private TypedArray obtainStyledAttributes;

        public SkinTypedArrayWrapper(Context context, TypedArray obtainStyledAttributes) {
            this.context = context;
            this.obtainStyledAttributes = obtainStyledAttributes;
        }

        @Override
        public boolean getBoolean(int i) {
            return obtainStyledAttributes.getBoolean(i, false);
        }

        @Override
        public int getColor(int i) {
            return SkinCompatResources.getColor(context, obtainStyledAttributes.getResourceId(i, 0));
        }

        @Nullable
        @Override
        public ColorStateList getColorStateList(int i) {
            return SkinCompatResources.getColorStateList(context, obtainStyledAttributes.getResourceId(i, 0));
        }

        @Override
        public int getDimensionPixelSize(int i) {
            return obtainStyledAttributes.getDimensionPixelSize(i, -1);
        }

        @Nullable
        @Override
        public Drawable getDrawable(int i) {
            return SkinCompatResources.getDrawable(context, obtainStyledAttributes.getResourceId(i, 0));
        }

        @Override
        public float getFloat(int i) {
            return obtainStyledAttributes.getFloat(i, -1f);
        }

        @Nullable
        @Override
        public Typeface getFont(int i) {
            int resourceId = obtainStyledAttributes.getResourceId(i, 0);
            return ResourcesCompat.getFont(context, resourceId);
        }

        @Override
        public float getFraction(int i, int b, int p) {
            return obtainStyledAttributes.getFraction(i, b, p, -1f);
        }

        @Override
        public int getIndex(int i) {
            return obtainStyledAttributes.getIndex(i);
        }

        @Override
        public int getIndexCount() {
            return obtainStyledAttributes.getIndexCount();
        }

        @Override
        public int getInt(int i) {
            return obtainStyledAttributes.getInt(i, -1);
        }

        @Override
        public int getLayoutDimension(int i) {
            return obtainStyledAttributes.getLayoutDimension(i, -1);
        }

        @Override
        public int getResourceId(int i) {
            return obtainStyledAttributes.getResourceId(i, 0);
        }

        @Nullable
        @Override
        public String getString(int i) {
            return obtainStyledAttributes.getString(i);
        }

        @NotNull
        @Override
        public Style getStyle(int i) {
            return new SkinResourceStyle(getResourceId(i));
        }

        @Nullable
        @Override
        public CharSequence getText(int i) {
            return obtainStyledAttributes.getText(i);
        }

        @Nullable
        @Override
        public CharSequence[] getTextArray(int i) {
            return obtainStyledAttributes.getTextArray(i);
        }

        @Override
        public boolean hasValue(int i) {
            return obtainStyledAttributes.hasValue(i);
        }

        @Override
        public void recycle() {
            obtainStyledAttributes.recycle();
        }
    }
}
