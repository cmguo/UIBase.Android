package com.xhb.uibase.binding;

import android.content.Context;

import androidx.databinding.BindingAdapter;
import androidx.viewpager.widget.ViewPager;

import com.xhb.uibase.view.list.ItemBindings;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.abs.IPagerNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;

import com.xhb.uibase.view.list.Items;
import com.xhb.uibase.view.list.PagerIndicators;
import com.xhb.uibase.view.XHBTabAdapter;

import java.util.List;

public class MagicIndicatorBindingAdapter {

    @BindingAdapter(value = {"titles", "itemBinding", "indicator", "navigator", "itemClicked", "viewPager"}, requireAll = false)
    public static <T> void setMagicIndicatorTitles(MagicIndicator magicIndicator, Object titles, Object itemBinding,
                                                   Object indicator, Object navigator,
                                                   XHBTabAdapter.TitleSelectListener listener, Object viewPagerOrId) {
        if (titles != null && itemBinding != null) {
            Context context = magicIndicator.getContext();
            XHBTabAdapter<T> adapter = new XHBTabAdapter(Items.get(context, titles), ItemBindings.get(context, itemBinding),
                PagerIndicators.get(context, indicator));
            if (listener != null) {
                ((XHBTabAdapter) adapter).setListener(listener);
            } else if (viewPagerOrId != null) {
                ViewPager viewPager = null;
                if (viewPagerOrId instanceof ViewPager)
                    viewPager = (ViewPager) viewPagerOrId;
                else
                    viewPager = magicIndicator.getRootView().findViewById((Integer) viewPagerOrId);
                ((XHBTabAdapter) adapter).bindViewPager(viewPager);
                ViewPagerHelper.bind(magicIndicator, viewPager);
            }
            if (magicIndicator.getNavigator() instanceof CommonNavigator) {
                ((CommonNavigator) magicIndicator.getNavigator()).setAdapter(adapter);
            } else {
                CommonNavigator indicatorNavigator = new CommonNavigator(magicIndicator.getContext());
                indicatorNavigator.setAdapter(adapter);
                magicIndicator.setNavigator(indicatorNavigator);
            }
        }
    }

    @BindingAdapter("adapter")
    public static void setMagicIndicatorAdapter(MagicIndicator magicIndicator, CommonNavigatorAdapter adapter) {
        if (magicIndicator != null && adapter != null) {
            CommonNavigator indicatorNavigator = new CommonNavigator(magicIndicator.getContext());
            indicatorNavigator.setAdapter(adapter);
            magicIndicator.setNavigator(indicatorNavigator);
        }
    }

    @BindingAdapter("navigator")
    public static void setMagicIndicatorNavigator(MagicIndicator magicIndicator, IPagerNavigator navigator) {
        if (magicIndicator != null && navigator != null) {
            magicIndicator.setNavigator(navigator);
        }
    }

}
