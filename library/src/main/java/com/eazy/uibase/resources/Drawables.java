package com.eazy.uibase.resources;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;

import java.util.Set;
import java.util.TreeSet;

public class Drawables {

    public static boolean isPureColor(Drawable drawable) {
        if (drawable instanceof VectorDrawable)
            return true;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
            bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getHeight(), bitmap.getWidth());
            Set<Integer> colors = new TreeSet<>();
            for (int p : pixels) {
                if (!colors.contains(p)) {
                    colors.add(p);
                }
            }
            return colors.size()  == 2;
        }
        return false;
    }

}
