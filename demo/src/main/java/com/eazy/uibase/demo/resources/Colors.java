package com.eazy.uibase.demo.resources;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StateSet;
import android.util.Xml;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.eazy.uibase.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class Colors extends Resources {

    private static final String TAG = "Colors";

    public static Map<String, ResourceValue> stdDynamicColors(Context context) {
        return getColors(context, R.color.class, Pattern.compile("^[a-z]{2,}_\\d+$"), true);
    }

    public static Map<String, ResourceValue> stdStaticColors(Context context) {
        return getColors(context, R.color.class, Pattern.compile("^static_([a-z]{2,}_)+\\d+$"), false);
    }

    public static Map<String, ResourceValue> getColors(Context context, Class<?> clazz, Pattern pattern, boolean dayNightOnly) {
        return getResources(context.getResources(), clazz, pattern, dayNightOnly);
    }

    public static int invert(int color) {
        return ((color) & 0xff000000) | (~(color & 0x00ffffff));
    }

    public static String text(int color) {
        return "#" + Integer.toHexString(color);
    }

    public static class StateColor {

        public String stateName = "";
        public int[] state;
        public String colorName = "";
        public int color;

    }

    public static Map<String, StateColor[]> stateListColors(Context context) {
        Map<String, ResourceValue> colors = getColors(context, R.color.class, Pattern.compile("^[a-z]{2,}_\\d+(_[a-z]{2,}ed\\d?)+$"), false);
        Map<String, StateColor[]> stateColors = new TreeMap<>();
        for (Map.Entry<String, ResourceValue> e : colors.entrySet()) {
            stateColors.put(e.getKey(), stateColors(context, e.getValue()));
        }
        return stateColors;
    }

    private static StateColor[] stateColors(Context context, ResourceValue value) {
        try {
            XmlPullParser xml = context.getResources().getXml(value.getResId());
            return obtainStateColors(context.getResources(), xml);
        } catch (Throwable e) {
            Log.w(TAG, e);
            return new StateColor[0];
        }
    }

    @NonNull
    private static StateColor[] obtainStateColors(@NonNull android.content.res.Resources r, @NonNull XmlPullParser parser) throws XmlPullParserException, IOException {
        final AttributeSet attrs = Xml.asAttributeSet(parser);

        int type;
        while ((type = parser.next()) != XmlPullParser.START_TAG
            && type != XmlPullParser.END_DOCUMENT) {
            // Seek parser to start tag.
        }

        if (type != XmlPullParser.START_TAG) {
            throw new XmlPullParserException("No start tag found");
        }

        return obtainStateColorsInner(r, parser, attrs);
    }

    /**
     * Create from inside an XML document. Called on a parser positioned at a
     * tag in an XML document, tries to create a ColorStateList from that tag.
     *
     * @return A new color state list for the current tag.
     * @throws XmlPullParserException if the current tag is not &lt;selector>
     */
    @NonNull
    private static StateColor[] obtainStateColorsInner(@NonNull android.content.res.Resources r, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs)
        throws XmlPullParserException, IOException {
        final String name = parser.getName();
        if (!name.equals("selector")) {
            throw new XmlPullParserException(
                parser.getPositionDescription() + ": invalid color state list tag " + name);
        }

        final int innerDepth = parser.getDepth() + 1;
        int depth;
        int type;

        StateColor[] stateColors = new StateColor[20];
        int listSize = 0;

        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT
            && ((depth = parser.getDepth()) >= innerDepth || type != XmlPullParser.END_TAG)) {
            if (type != XmlPullParser.START_TAG || depth > innerDepth
                || !parser.getName().equals("item")) {
                continue;
            }

            final TypedArray a = r.obtainAttributes(attrs, R.styleable.ColorStateListItem);
            final int baseColor = a.getColor(R.styleable.ColorStateListItem_android_color,
                Color.MAGENTA);

            float alphaMod = 1.0f;
            if (a.hasValue(R.styleable.ColorStateListItem_android_alpha)) {
                alphaMod = a.getFloat(R.styleable.ColorStateListItem_android_alpha, alphaMod);
            } else if (a.hasValue(R.styleable.ColorStateListItem_alpha)) {
                alphaMod = a.getFloat(R.styleable.ColorStateListItem_alpha, alphaMod);
            }

            a.recycle();
            // Parse all unrecognized attributes as state specifiers.
            int j = 0;
            final int numAttrs = attrs.getAttributeCount();
            StateColor stateColor = new StateColor();
            stateColor.state = new int[numAttrs];
            stateColor.color = modulateColorAlpha(baseColor, alphaMod);
            for (int i = 0; i < numAttrs; i++) {
                final int stateResId = attrs.getAttributeNameResource(i);
                if (stateResId == android.R.attr.color) {
                    stateColor.colorName = getAttributeValue(r, attrs, i) + stateColor.colorName;
                } else if (stateResId == android.R.attr.alpha || stateResId == R.attr.alpha) {
                    stateColor.colorName = stateColor.colorName + "\nalpha: " + getAttributeValue(r, attrs, i);
                } else {
                    // Unrecognized attribute, add to state set
                    boolean set = attrs.getAttributeBooleanValue(i, false);
                    stateColor.state[j++] = set ? stateResId : -stateResId;
                    stateColor.stateName = stateColor.stateName + "\n"
                        + simpleName(r.getResourceName(stateResId)).replace("state_", "")
                        + "=" + set;
                }
            }
            stateColor.state = StateSet.trimStateSet(stateColor.state, j);
            if (stateColor.stateName.isEmpty()) {
                stateColor.stateName = "<default>";
            } else {
                stateColor.stateName = stateColor.stateName.substring(1);
            }

            // Apply alpha modulation. If we couldn't resolve the color or
            // alpha yet, the default values leave us enough information to
            // modulate again during applyTheme().
            stateColors = appendArray(stateColors, listSize, stateColor);
            listSize++;
        }

        StateColor[] colors = new StateColor[listSize];
        System.arraycopy(stateColors, 0, colors, 0, listSize);

        return colors;
    }

    private static String getAttributeValue(@NonNull android.content.res.Resources r, @NonNull AttributeSet attrs, int index) {
        int resId = attrs.getAttributeResourceValue(index, 0);
        if (resId != 0) {
            return simpleName(r.getResourceName(resId));
        }
        return attrs.getAttributeValue(index);
    }

    private static <T> T[] appendArray(T[] array, int currentSize, T element) {
        assert currentSize <= array.length;

        if (currentSize + 1 > array.length) {
            T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(),
                growSize(currentSize));
            System.arraycopy(array, 0, newArray, 0, currentSize);
            array = newArray;
        }
        array[currentSize] = element;
        return array;
    }

    private static int growSize(int currentSize) {
        return currentSize <= 4 ? 8 : currentSize * 2;
    }

    @ColorInt
    private static int modulateColorAlpha(@ColorInt int color,
                                          @FloatRange(from = 0f, to = 1f) float alphaMod) {
        int alpha = Math.round(Color.alpha(color) * alphaMod);
        return (color & 0x00ffffff) | (alpha << 24);
    }
}
