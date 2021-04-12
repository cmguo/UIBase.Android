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
    private int mEndianSize; // also padding head and tail

    private boolean mSizeConverted = false;

    public DividerDecoration(int orientation, float size) {
        this(orientation, size, 0, 0f);
    }

    public DividerDecoration(int orientation, float size, float endianSize) {
        this(orientation, size, 0, endianSize);
    }

    public DividerDecoration(int orientation, float size, int color) {
        this(orientation, size, 0, 0);
    }

    public DividerDecoration(int orientation, float size, int color, float endianSize) {
        if (color != 0)
            mDivider = new ColorDrawable(color);
        mOrientation = orientation;
        mSize = (int) size;
        mEndianSize = (int) endianSize;
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
        int childCount = parent.getChildCount();
        if (childCount == 0)
            return;

        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int recyclerViewTop = parent.getPaddingTop();
        final int recyclerViewBottom = parent.getHeight() - parent.getPaddingBottom();

        if (mEndianSize > 0) {
            final int top = recyclerViewTop;
            final int bottom = Math.min(recyclerViewBottom, top + mEndianSize);
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = Math.max(recyclerViewTop, child.getBottom() + params.bottomMargin);
            final int bottom = Math.min(recyclerViewBottom, top + mSize);
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
        if (mEndianSize > 0) {
            final View child = parent.getChildAt(childCount - 1);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = Math.max(recyclerViewTop, child.getBottom() + params.bottomMargin);
            final int bottom = Math.min(recyclerViewBottom, top + mEndianSize);
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        if (childCount == 0)
            return;

        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int recyclerViewLeft = parent.getPaddingLeft();
        final int recyclerViewRight = parent.getWidth() - parent.getPaddingRight();
        if (mEndianSize > 0) {
            final int left = recyclerViewLeft;
            final int right = Math.min(recyclerViewRight, left + mEndianSize);
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = Math.max(recyclerViewLeft, child.getRight() + params.rightMargin);
            final int right = Math.min(recyclerViewRight, left + mSize);
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
        if (mEndianSize > 0) {
            final View child = parent.getChildAt(childCount - 1);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                .getLayoutParams();
            final int left = Math.max(recyclerViewLeft, child.getRight() + params.rightMargin);
            final int right = Math.min(recyclerViewRight, left + mEndianSize);
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
        if (mEndianSize > 0) {
            if (position == 0) {
                if (mOrientation == LinearLayoutManager.VERTICAL) {
                    outRect.set(0, mEndianSize, 0, mSize);
                } else {
                    outRect.set(mEndianSize, 0, mSize, 0);
                }
                return;
            } else if (position == count - 1) {
                if (mOrientation == LinearLayoutManager.VERTICAL) {
                    outRect.set(0, 0, 0, mEndianSize);
                } else {
                    outRect.set(0, 0, mEndianSize, 0);
                }
                return;
            }
        }
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