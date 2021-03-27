package com.xhb.uibase.view.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;

public class PagerIndicators {

    public static IPagerIndicator get(Context context, Object viewOrLayout) {
        if (viewOrLayout == null)
            return null;
        if (viewOrLayout instanceof IPagerIndicator)
            return (IPagerIndicator) viewOrLayout;
        return (IPagerIndicator) LayoutInflater.from(context).inflate((Integer) viewOrLayout, new LinearLayout(context), false);
    }

}
