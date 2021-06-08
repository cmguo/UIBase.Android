package com.eazy.uibase.view.list;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.StyleRes;
import androidx.recyclerview.widget.RecyclerView;

import com.eazy.uibase.resources.RoundDrawable;

import org.jetbrains.annotations.NotNull;

public class BackgroundDecoration extends BaseDecoration {

    private Drawable mBackground;
    private Rect mOutRect = new Rect();

    public BackgroundDecoration(Context context, float radius, int color) {
        this(context, radius, color, false);
    }

    public BackgroundDecoration(Context context, float radius, int color, boolean outer) {
        mBackground = new RoundDrawable(ColorStateList.valueOf(color), radius);
        if (outer) {
            int r = (int) radius;
            mOutRect.set(r, r, r, r);
        }
    }

    public BackgroundDecoration(Drawable drawable, Rect outRect) {
        mBackground = drawable;
        mOutRect = outRect;
    }

    public BackgroundDecoration(Context context, @StyleRes int roundStyle) {
        this(context, roundStyle, false);
    }

    public BackgroundDecoration(Context context, @StyleRes int roundStyle, boolean outer) {
        RoundDrawable roundDrawable = new RoundDrawable(context, roundStyle);
        mBackground = roundDrawable;
        mOutRect = new Rect();
        roundDrawable.getPadding().round(mOutRect);
        if (outer) {
            float[] radii = roundDrawable.getCornerRadii();
            if (radii != null) { // left, top, right, top, right, bottom, left, bottom
                mOutRect.left += ceil(Math.max(radii[0], radii[6]));
                mOutRect.top += ceil(Math.max(radii[1], radii[3]));
                mOutRect.right += ceil(Math.max(radii[2], radii[4]));
                mOutRect.bottom += ceil(Math.max(radii[5], radii[7]));
            } else {
                int r = ceil(roundDrawable.getCornerRadius());
                mOutRect.offset(r, r);
            }
        }
    }

    @Override
    protected void drawChildDecoration(@NotNull Canvas c, Rect outRect, Rect childRect, Rect parentRect) {
        mBackground.setBounds(outRect);
        mBackground.draw(c);
    }

    @Override
    public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        outRect.set(mOutRect);
    }

    private static int ceil(float f) {
        return (int)(f > 0 ? Math.ceil(f) : Math.floor(f));
    }
}