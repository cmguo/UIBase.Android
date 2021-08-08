package com.eazy.uibase.view.list;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import org.jetbrains.annotations.NotNull;

public class DividerDecoration extends BaseDecoration {

    private Drawable mDivider;
    private final int mSize;
    private final int mEndianSize; // also padding head and tail

    public DividerDecoration(float size) {
        this(size, 0, 0f);
    }

    public DividerDecoration(float size, float endianSize) {
        this(size, 0, endianSize);
    }

    public DividerDecoration(float size, int color) {
        this(size, color, 0);
    }

    public DividerDecoration(float size, int color, float endianSize) {
        if (color != 0)
            mDivider = new ColorDrawable(color);
        mSize = ceil(size);
        mEndianSize = ceil(endianSize);
    }

    public void updateColor(int color) {
        mDivider = new ColorDrawable(color);
    }

    @Override
    public void onDraw(@NotNull Canvas c, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        if (mDivider == null)
            return;
        super.onDraw(c, parent, state);
    }

    @Override
    protected void getItemOffsets(Rect outRect, int type) {
        if (orientation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, mSize);
        } else {
            outRect.set(0, 0, mSize, 0);
        }
    }

    @Override
    protected void getTreeOffsets(Rect treeRect, int[] position, int level) {
        if (level == 0) {
            if (orientation == LinearLayoutManager.VERTICAL) {
                treeRect.set(0, mEndianSize, 0, mEndianSize);
            } else {
                treeRect.set(mEndianSize, 0, mEndianSize, 0);
            }
        }
    }

    private final Rect temp = new Rect();

    @Override
    protected void drawChildDecoration(@NotNull Canvas c, @NotNull View child, int type) {
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