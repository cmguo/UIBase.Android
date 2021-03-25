package com.xhb.uibase.binding;

import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.xhb.uibase.view.list.FragmentBindingPagerAdapter;
import com.xhb.uibase.view.list.QuickUtils;

import java.util.List;
import java.util.Map;

public class ViewPagerBindingAdapter {

    @BindingAdapter("fragmentAdapter")
    public static void bindFragmentAdapter(ViewPager viewPager, androidx.viewpager.widget.PagerAdapter adapter) {
        if (viewPager != null && adapter != null) {
            viewPager.setAdapter(adapter);
        }
    }

    @BindingAdapter("position")
    public static void bindPosition(ViewPager viewPager, final int position) {
        if (viewPager.getAdapter() != null && viewPager.getAdapter().getCount() > position) {
            viewPager.setCurrentItem(position);
        }
    }

    @BindingAdapter(value = {"data", "template", "preload"})
    public static <T, K> void bindFragmentAdapter(ViewPager viewPager, List<T> data, Map<K, Class<? extends Fragment>> template, boolean preload) {
        if (viewPager != null && data != null && template != null) {
            FragmentManager fragmentManager = null;
            if (fragmentManager == null) {
                if (!QuickUtils.isViewAddedToParent(viewPager)) {
                    throw new IllegalStateException("View has not been added to parent, " +
                        "can not use this binding in xml, instead you should use app:fragmentAdapter instead.");
                }
                Object o = QuickUtils.tryFindFragmentOrActivity(viewPager);
                if (o instanceof FragmentActivity) {
                    fragmentManager = ((FragmentActivity) o).getSupportFragmentManager();
                } else if (o instanceof Fragment) {
                    fragmentManager = ((Fragment) o).getChildFragmentManager();
                } else {
                    throw new RuntimeException("Can not find FragmentManager automatic, " +
                        "can not use this binding in xml, instead you should use app:fragmentAdapter instead.");
                }
            }
            PagerAdapter adapter = new FragmentBindingPagerAdapter(fragmentManager, (List<Object>) data, (Map<Object, Class<? extends Fragment>>) template, preload);
            viewPager.setAdapter(adapter);
        }
    }
}
