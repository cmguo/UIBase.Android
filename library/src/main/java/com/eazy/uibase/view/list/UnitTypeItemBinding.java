package com.eazy.uibase.view.list;

import android.content.Context;

public class UnitTypeItemBinding<T> extends BaseItemBinding<T> {

    private int mItemLayout;

    public UnitTypeItemBinding(int itemLayout) {
        mItemLayout = itemLayout;
    }

    public UnitTypeItemBinding(Context context, int itemLayout) {
        super(context);
        mItemLayout = itemLayout;
    }

    @Override
    public int getItemViewType(T item) {
        return mItemLayout;
    }
}
