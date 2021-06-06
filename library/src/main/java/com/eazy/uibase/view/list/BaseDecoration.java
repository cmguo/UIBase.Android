package com.eazy.uibase.view.list;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class BaseDecoration extends RecyclerView.ItemDecoration {

    private final Rect parentRect = new Rect();
    private final Rect childRect = new Rect();
    private final Rect outRect = new Rect();

    @Override
    public void onDraw(@NotNull Canvas c, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int childCount = parent.getChildCount();
        if (childCount == 0)
            return;

        parentRect.set(parent.getPaddingLeft(), parent.getPaddingTop(),
            parent.getWidth() - parent.getPaddingRight(),
            parent.getHeight() - parent.getPaddingBottom());

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            childRect.set(
                child.getLeft() - params.leftMargin,
                child.getTop() - params.topMargin,
                child.getRight() + params.rightMargin,
                child.getBottom() + params.bottomMargin);
            getItemOffsets(outRect, child, parent, state);
            outRect.set(childRect.left - outRect.left, childRect.top - outRect.top,
                childRect.right + outRect.right, childRect.bottom + outRect.bottom);
            drawChildDecoration(c, outRect, childRect, parentRect);
        }
    }

    protected void drawChildDecoration(@NotNull Canvas c, Rect outRect, Rect childRect, Rect parentRect) {
    }

}