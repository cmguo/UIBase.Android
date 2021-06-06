package com.eazy.uibase.view.list;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DividerDecoration extends BaseDecoration {

    private Drawable mDivider;
    private final int mOrientation;
    private int mSize;
    private final int mEndianSize; // also padding head and tail

    public DividerDecoration(Context context, int orientation, float size) {
        this(context, orientation, size, 0, 0f);
    }

    public DividerDecoration(Context context, int orientation, float size, float endianSize) {
        this(context, orientation, size, 0, endianSize);
    }

    public DividerDecoration(Context context, int orientation, float size, int color) {
        this(context, orientation, size, color, 0);
    }

    public DividerDecoration(Context context, int orientation, float size, int color, float endianSize) {
        if (color != 0)
            mDivider = new ColorDrawable(color);
        mOrientation = orientation;
        float density = context.getResources().getDisplayMetrics().density;
        mSize = ceil(size * density);
        mEndianSize = ceil(endianSize * density);
    }

    @Override
    public void onDraw(@NotNull Canvas c, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        if (mDivider == null)
            return;
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
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

    private Rect temp = new Rect();

    @Override
    protected void drawChildDecoration(@NotNull Canvas c, Rect outRect, Rect childRect, Rect parentRect) {
        temp.set(outRect);
        if (temp.left < childRect.left) {
            temp.right = childRect.left;
            mDivider.setBounds(temp);
            temp.right = outRect.right;
            mDivider.draw(c);
        }
        if (temp.right > childRect.right) {
            temp.left = childRect.right;
            mDivider.setBounds(temp);
            temp.left = outRect.left;
            mDivider.draw(c);
        }
        if (temp.top < childRect.top) {
            temp.bottom = childRect.top;
            mDivider.setBounds(temp);
            temp.bottom = outRect.bottom;
            mDivider.draw(c);
        }
        if (temp.bottom > childRect.bottom) {
            temp.top = childRect.bottom;
            mDivider.setBounds(temp);
            temp.top = outRect.top;
            mDivider.draw(c);
        }
    }

    private static int ceil(float f) {
        return (int)(f > 0 ? Math.ceil(f) : Math.floor(f));
    }

}