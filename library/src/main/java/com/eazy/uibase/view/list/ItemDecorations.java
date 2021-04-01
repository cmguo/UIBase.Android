package com.eazy.uibase.view.list;


import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ItemDecorations {

    public static  RecyclerView.ItemDecoration divider(int orientation, int size) {
        return new DividerDecoration(orientation, size);
    }

    public static  RecyclerView.ItemDecoration divider(int orientation, int size, int color) {
        return new DividerDecoration(orientation, size, color);
    }
}