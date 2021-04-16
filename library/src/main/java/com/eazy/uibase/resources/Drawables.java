package com.eazy.uibase.resources;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Drawables {

    /*
        Group pixels with color rgb range <= 16
        if max size two group cover 95%, then consider to be pure
     */
    public static boolean isPureColor(Drawable drawable) {
        if (drawable instanceof VectorDrawable)
            return true;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
            bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getHeight(), bitmap.getWidth());
            Map<Integer, Integer> colors = new TreeMap<>();
            for (int p : pixels) {
                Integer c = colors.get(p);
                if (c == null)
                    c = 1;
                else
                    c = c + 1;
                colors.put(p, c);
            }
            if (colors.size() <= 2)
                return true;
            List<Group> groups = new ArrayList<>();
            for (Map.Entry<Integer, Integer> e : colors.entrySet()) {
                boolean added = false;
                for (Group g : groups) {
                    if (g.add(e.getKey(), e.getValue())) {
                        added = true;
                        break;
                    }
                }
                if (!added) {
                    groups.add(new Group(e.getKey(), e.getValue()));
                }
            }
            if (groups.size() <= 2)
                return true;
            Group max = Collections.max(groups, (l, r) -> l.cnt - r.cnt );
            groups.remove(max);
            Group max2 = Collections.max(groups, (l, r) -> l.cnt - r.cnt );
            return (max.cnt + max2.cnt) * 100 > pixels.length * 95;
        }
        return false;
    }

    static class Group {
        int min;
        int max;
        int cnt;

        Group(int c, int n) {
            min = max = c;
            cnt = n;
        }

        boolean add(int c, int n) {
            int mask = 0xff;
            int unit = 0x10;
            int min2 = min;
            int max2 = max;
            for (int i = 0; i < 3; ++i) {
                int v = c & mask;
                if (v < (min & mask)) {
                    if (v + unit < (max & mask)) {
                        return false;
                    } else {
                        min2 = (min & ~mask) | v;
                    }
                } else if (v > (max & mask)) {
                    if (v < (min & mask) + unit) {
                        max2 = (max & ~mask) | v;
                    } else {
                        return false;
                    }
                }
                mask <<= 8;
                unit <<= 8;
            }
            min = min2;
            max = max2;
            cnt += n;
            return true;
        }
    }

}
