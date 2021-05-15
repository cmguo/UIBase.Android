package com.eazy.uibase.widget.banner;

import androidx.viewpager.widget.ViewPager;

import com.eazy.uibase.widget.banner.transformer.AccordionTransformer;
import com.eazy.uibase.widget.banner.transformer.BackgroundToForegroundTransformer;
import com.eazy.uibase.widget.banner.transformer.CubeInTransformer;
import com.eazy.uibase.widget.banner.transformer.CubeOutTransformer;
import com.eazy.uibase.widget.banner.transformer.DefaultTransformer;
import com.eazy.uibase.widget.banner.transformer.DepthPageTransformer;
import com.eazy.uibase.widget.banner.transformer.FlipHorizontalTransformer;
import com.eazy.uibase.widget.banner.transformer.FlipVerticalTransformer;
import com.eazy.uibase.widget.banner.transformer.ForegroundToBackgroundTransformer;
import com.eazy.uibase.widget.banner.transformer.RotateDownTransformer;
import com.eazy.uibase.widget.banner.transformer.RotateUpTransformer;
import com.eazy.uibase.widget.banner.transformer.ScaleInOutTransformer;
import com.eazy.uibase.widget.banner.transformer.StackTransformer;
import com.eazy.uibase.widget.banner.transformer.TabletTransformer;
import com.eazy.uibase.widget.banner.transformer.ZoomInTransformer;
import com.eazy.uibase.widget.banner.transformer.ZoomOutSlideTransformer;
import com.eazy.uibase.widget.banner.transformer.ZoomOutTransformer;

public class Transformer {
    public static Class<? extends ViewPager.PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ZoomOut = ZoomOutTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
