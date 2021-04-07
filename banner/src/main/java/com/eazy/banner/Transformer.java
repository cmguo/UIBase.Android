package com.eazy.banner;

import androidx.viewpager.widget.ViewPager;

import com.eazy.banner.transformer.AccordionTransformer;
import com.eazy.banner.transformer.BackgroundToForegroundTransformer;
import com.eazy.banner.transformer.CubeInTransformer;
import com.eazy.banner.transformer.CubeOutTransformer;
import com.eazy.banner.transformer.DefaultTransformer;
import com.eazy.banner.transformer.DepthPageTransformer;
import com.eazy.banner.transformer.FlipHorizontalTransformer;
import com.eazy.banner.transformer.FlipVerticalTransformer;
import com.eazy.banner.transformer.ForegroundToBackgroundTransformer;
import com.eazy.banner.transformer.RotateDownTransformer;
import com.eazy.banner.transformer.RotateUpTransformer;
import com.eazy.banner.transformer.ScaleInOutTransformer;
import com.eazy.banner.transformer.StackTransformer;
import com.eazy.banner.transformer.TabletTransformer;
import com.eazy.banner.transformer.ZoomInTransformer;
import com.eazy.banner.transformer.ZoomOutSlideTransformer;
import com.eazy.banner.transformer.ZoomOutTransformer;

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
