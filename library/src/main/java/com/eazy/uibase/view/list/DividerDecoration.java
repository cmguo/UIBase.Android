package com.eazy.uibase.view.list;


import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class DividerDecoration extends RecyclerView.ItemDecoration {


    private Drawable mDivider;
    private final int mOrientation;
    private int mSize;
    private boolean mSizeConverted = false;

    public DividerDecoration(int orientation, int size) {
        this(orientation, size, 0);
    }

    public DividerDecoration(int orientation, int size, int color) {
        if (color != 0)
            mDivider = new ColorDrawable(color);
        mOrientation = orientation;
        mSize = size;
    }

    @Override
    public void onDraw(@NotNull Canvas c, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mDivider == null)
            return;
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }


    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int recyclerViewTop = parent.getPaddingTop();
        final int recyclerViewBottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();

        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = Math.max(recyclerViewTop, child.getBottom() + params.bottomMargin);
            final int bottom = Math.min(recyclerViewBottom, top + mSize);
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int recyclerViewLeft = parent.getPaddingLeft();
        final int recyclerViewRight = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = Math.max(recyclerViewLeft, child.getRight() + params.rightMargin);
            final int right = Math.min(recyclerViewRight, left + mSize);
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        if (!mSizeConverted) {
            float density = parent.getContext().getResources().getDisplayMetrics().density;
            mSize = ceil(mSize * density);
            mSizeConverted = true;
        }
        int position = parent.getChildAdapterPosition(view);
        int count = Objects.requireNonNull(parent.getAdapter()).getItemCount();
        if (position < count - 1) {
            if (mOrientation == LinearLayoutManager.VERTICAL) {
                outRect.set(0, 0, 0, mSize);
            } else {
                outRect.set(0, 0, mSize, 0);
            }
        } else {
            outRect.set(0, 0, 0, 0);
        }
    }

    private static int ceil(float f) {
        return (int)(f > 0 ? Math.ceil(f) : Math.floor(f));
    }

}