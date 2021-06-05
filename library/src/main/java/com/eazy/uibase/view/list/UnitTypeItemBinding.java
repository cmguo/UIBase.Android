package com.eazy.uibase.view.list;

import android.content.Context;
import android.view.View;

public class UnitTypeItemBinding extends BaseItemBinding {

    private int mItemLayout;

    public UnitTypeItemBinding(int itemLayout) {
        mItemLayout = itemLayout;
    }

    public UnitTypeItemBinding(Context context, int itemLayout) {
        super(context);
        mItemLayout = itemLayout;
    }

    @Override
    public int getItemViewType(Object item) {
        return mItemLayout;
    }

    @Override
    protected void bindView(View view, Object item, int position) {
        super.bindView(view, item, position);
    }
}
