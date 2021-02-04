package com.xhb.uibase.demo.view.recycler;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class PaddingDecoration extends RecyclerView.ItemDecoration{

    private int padding;

    public PaddingDecoration() {
        //即你要设置的分割线的宽度 --这里设为10dp
        padding = 10;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //outRect就是你那个item条目的矩形
        outRect.left = padding;  //相当于 设置 left padding
        outRect.top = padding;   //相当于 设置 top padding
        outRect.right = padding; //相当于 设置 right padding
        outRect.bottom = padding;  //相当于 设置 bottom padding
    }
}