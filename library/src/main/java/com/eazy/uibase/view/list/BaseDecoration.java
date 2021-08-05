package com.eazy.uibase.view.list;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class BaseDecoration extends RecyclerView.ItemDecoration {

    protected final Rect parentRect = new Rect();
    protected final Rect childRect = new Rect();
    protected final Rect outRect = new Rect();
    protected int orientation;
    protected RecyclerView.State state;

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (!(layoutManager instanceof LinearLayoutManager))
            return;
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
        orientation = linearLayoutManager.getOrientation();
        this.state = state;
        RecyclerView.Adapter<?> adapter = parent.getAdapter();
        if (adapter == null)
            return;
        int position = parent.getChildAdapterPosition(view);
        int type = adapter.getItemViewType(position);
        getItemOffsets(outRect, type);
    }


    protected void getItemOffsets(Rect outRect, int type) {
    }

    @Override
    public void onDraw(@NotNull Canvas c, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (!(layoutManager instanceof LinearLayoutManager))
            return;
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
        orientation = linearLayoutManager.getOrientation();
        RecyclerView.Adapter<?> adapter = parent.getAdapter();
        if (adapter == null)
            return;

        int childCount = parent.getChildCount();
        if (childCount == 0)
            return;

        parentRect.set(parent.getPaddingLeft(), parent.getPaddingTop(),
            parent.getWidth() - parent.getPaddingRight(),
            parent.getHeight() - parent.getPaddingBottom());

        int first = linearLayoutManager.findFirstVisibleItemPosition();
        int last = linearLayoutManager.findLastVisibleItemPosition();

        for (int i = first; i <= last; i++) {
            final View child = linearLayoutManager.findViewByPosition(i);
            if (child == null)
                continue;
            getChildRect(child);
            int type = adapter.getItemViewType(i);
            getItemOffsets(outRect, type);
            outRect.set(childRect.left - outRect.left, childRect.top - outRect.top,
                childRect.right + outRect.right, childRect.bottom + outRect.bottom);
            drawChildDecoration(c, child, type);
        }
    }

    private void getChildRect(View child) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
        childRect.set(
            child.getLeft() - params.leftMargin,
            child.getTop() - params.topMargin,
            child.getRight() + params.rightMargin,
            child.getBottom() + params.bottomMargin);
    }

    protected void drawChildDecoration(@NotNull Canvas c, @NotNull View child, int type) {
    }

}