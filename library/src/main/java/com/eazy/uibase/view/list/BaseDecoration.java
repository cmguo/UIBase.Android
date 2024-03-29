package com.eazy.uibase.view.list;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class BaseDecoration extends RecyclerView.ItemDecoration {

    protected final Rect parentRect = new Rect();
    protected final Rect childRect = new Rect();
    protected final Rect outRect = new Rect();
    protected int orientation;
    protected RecyclerView.State state;

    private final Rect treeRect = new Rect();
    private final Rect outRect2 = new Rect();

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        RecyclerView.Adapter<?> adapter = parent.getAdapter();
        if (adapter == null)
            return;
        if (adapter instanceof RecyclerViewAdapter) {
            if (((RecyclerViewAdapter) adapter).getRealItemCount() == 0) {
                return;
            }
        }
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (!(layoutManager instanceof LinearLayoutManager))
            return;
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
        orientation = linearLayoutManager.getOrientation();
        this.state = state;
        RecyclerViewTreeAdapter treeAdapter = null;
        if (adapter instanceof RecyclerViewTreeAdapter)
            treeAdapter = (RecyclerViewTreeAdapter) adapter;

        int count = adapter.getItemCount();
        int position = parent.getChildAdapterPosition(view);
        int type = adapter.getItemViewType(position);
        getItemOffsets(outRect, type);

        if (treeAdapter != null) {
            int[] treePosition = treeAdapter.getTreePosition(position);
            getTreesOffsets(outRect, treeAdapter, treePosition, treePosition.length);
        } else {
            getTreeOffsets(treeRect, RecyclerViewTreeAdapter.RootPosition, 0);
            addTreeOffsets(outRect, treeRect, position > 0, position + 1 < count);
        }

        Log.d("BaseDecoration", "getItemOffsets: " + position + " -> " + outRect.toString());
    }

    private void getTreesOffsets(@NonNull Rect outRect, RecyclerViewTreeAdapter treeAdapter, int[] treePosition, int treeLevel) {
        boolean lowerMiddle = false;
        boolean upperMiddle = false;
        Iterable<?> children = treeAdapter.getChildren(treeAdapter.getRealItem(treePosition, treeLevel));
        if (children != null) {
            getTreeOffsets(treeRect, treePosition, treeLevel);
            upperMiddle = children.iterator().hasNext();
            addTreeOffsets(outRect, treeRect, false, upperMiddle);
        }
        int[] nextPosition = treeAdapter.getNextPosition(treePosition, treeLevel);
        if (nextPosition == null)
            nextPosition = RecyclerViewTreeAdapter.RootPosition;
        for (int i = treeLevel - 1; i >= 0; --i) {
            getTreeOffsets(treeRect, treePosition, i);
            lowerMiddle |= i > 0 || treePosition[i] > 0;
            upperMiddle |= i < nextPosition.length;
            addTreeOffsets(outRect, treeRect, lowerMiddle, upperMiddle);
        }
    }

    // call from onDraw, take item as boundary
    private void getTreesOffsets(@NonNull Rect outRect, RecyclerViewTreeAdapter treeAdapter, int[] treePosition, int treeLevel, int minLevel) {
        Iterable<?> children = treeAdapter.getChildren(treeAdapter.getRealItem(treePosition, treeLevel));
        if (children != null) {
            getTreeOffsets(treeRect, treePosition, treeLevel);
            addTreeOffsets(outRect, treeRect, false, false);
        }
        for (int i = treeLevel - 1; i >= minLevel; --i) {
            getTreeOffsets(treeRect, treePosition, i);
            addTreeOffsets(outRect, treeRect, false, false);
        }
        Log.d("BaseDecoration", "getTreesOffsets: "
                + Arrays.toString(Arrays.copyOf(treePosition, treeLevel))
                + ": " + minLevel
                + " -> " + outRect.toString());
    }

    private void addTreeOffsets(Rect outRect, Rect treeRect, boolean prev, boolean next) {
        if (prev) {
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                treeRect.left = 0;
            } else {
                treeRect.top = 0;
            }
        }
        if (next) {
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                treeRect.right = 0;
            } else {
                treeRect.bottom = 0;
            }
        }
        outRect.left += treeRect.left;
        outRect.top += treeRect.top;
        outRect.right += treeRect.right;
        outRect.bottom += treeRect.bottom;
    }

    protected void getItemOffsets(Rect outRect, int type) {
    }

    protected void getTreeOffsets(Rect treeRect, int[] position, int level) {
    }

    @Override
    public void onDraw(@NotNull Canvas c, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        RecyclerView.Adapter<?> adapter = parent.getAdapter();
        if (adapter == null)
            return;
        if (adapter instanceof RecyclerViewAdapter) {
            if (((RecyclerViewAdapter) adapter).getRealItemCount() == 0) {
                return;
            }
        }
        RecyclerViewTreeAdapter treeAdapter = null;
        if (adapter instanceof RecyclerViewTreeAdapter)
            treeAdapter = (RecyclerViewTreeAdapter) adapter;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (!(layoutManager instanceof LinearLayoutManager))
            return;
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
        orientation = linearLayoutManager.getOrientation();
        int childCount = parent.getChildCount();
        if (childCount == 0)
            return;

        parentRect.set(parent.getPaddingLeft(), parent.getPaddingTop(),
            parent.getWidth() - parent.getPaddingRight(),
            parent.getHeight() - parent.getPaddingBottom());

        int first = linearLayoutManager.findFirstVisibleItemPosition();
        int last = linearLayoutManager.findLastVisibleItemPosition();

        if (treeAdapter != null) {
            int[] firstPos = treeAdapter.getTreePosition(first);
            int[] lastPos = treeAdapter.getTreePosition(last);
            RecyclerViewTreeAdapter treeAdapter2 = treeAdapter;
            RecyclerViewTreeAdapter.TreeVisitor visitor = (firstPos2, firstLevel2, lastPos2, level) -> {
                Log.d("BaseDecoration", "visitTrees level " + level + ": "
                    + Arrays.toString(Arrays.copyOf(firstPos2, firstLevel2)) + " ~ " + Arrays.toString(lastPos2));
                int first2 = treeAdapter2.getItemPosition(firstPos2, firstLevel2);
                int last2 = treeAdapter2.getItemPosition(lastPos2);
                getChildRect(linearLayoutManager.findViewByPosition(first2));
                int type = adapter.getItemViewType(first2);
                outRect.setEmpty();
                getItemOffsets(outRect, type);
                getTreesOffsets(outRect, treeAdapter2, firstPos2, firstLevel2, level);
                outRect2.set(childRect.left - outRect.left, childRect.top - outRect.top,
                    childRect.right + outRect.right, childRect.bottom + outRect.bottom);
                getChildRect(linearLayoutManager.findViewByPosition(last2));
                int type2 = adapter.getItemViewType(last2);
                outRect.setEmpty();
                getItemOffsets(outRect, type2);
                getTreesOffsets(outRect, treeAdapter2, lastPos2, lastPos2.length, level);
                outRect.set(childRect.left - outRect.left, childRect.top - outRect.top,
                    childRect.right + outRect.right, childRect.bottom + outRect.bottom);
                outRect.set(Math.min(outRect2.left, outRect.left), Math.min(outRect2.top, outRect.top),
                    Math.max(outRect2.right, outRect.right), Math.max(outRect2.bottom, outRect.bottom));
                Log.d("BaseDecoration", "drawTreeDecoration level: " + level + ", outRect: " + outRect.toString());
                drawTreeDecoration(c, firstPos, level);
            };
            treeAdapter.visitTrees(visitor, firstPos, lastPos);
        }

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

    private void getChildRect(@Nullable View child) {
        if (child == null)
            return;
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
        childRect.set(
            child.getLeft() - params.leftMargin,
            child.getTop() - params.topMargin,
            child.getRight() + params.rightMargin,
            child.getBottom() + params.bottomMargin);
    }

    protected void drawChildDecoration(@NotNull Canvas c, @NotNull View child, int type) {
    }

    protected void drawTreeDecoration(@NotNull Canvas c, int[] position, int level) {
    }

}