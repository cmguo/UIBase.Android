package com.eazy.uibase.binding;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.eazy.uibase.widget.ZCarouselView;
import com.eazy.uibase.widget.banner.Banner;
import com.eazy.uibase.widget.banner.loader.ImageLoader;

public class ZCarouselViewBindingAdapter {

    @InverseBindingAdapter(attribute = "slideIndex", event = "slideIndexAttrChanged")
    public static int getSlideIndex(final ZCarouselView view) {
        return view.getSlideIndex();
    }

    @BindingAdapter(value = {"slideIndexChanged", "slideIndexAttrChanged"}, requireAll = false)
    public static void setSlideIndexChangedListener(final ZCarouselView view,
                                                   final ZCarouselView.OnSlideIndexChangeListener listener,
                                                   final InverseBindingListener dragStateAttrChanged) {
        if (listener != null) {
            view.setOnSlideIndexChangeListener(listener);
        } else if (dragStateAttrChanged != null) {
            view.setOnSlideIndexChangeListener((ZCarouselView Carousel, int index) -> {
                dragStateAttrChanged.onChange();
            });
        } else {
            view.setOnSlideIndexChangeListener(null);
        }
    }

    @BindingAdapter("imageLoader")
    public static void setImageLoader(final Banner view, ImageLoader loader) {
        view.setImageLoader(loader);
    }
}
