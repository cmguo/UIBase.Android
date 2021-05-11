package com.eazy.uibase.demo.core;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.eazy.uibase.demo.BR;
import com.eazy.uibase.demo.core.annotation.Author;
import com.eazy.uibase.demo.resources.Resources;
import com.eazy.uibase.resources.Drawables;

import java.util.ArrayList;
import java.util.List;

public class ComponentInfo extends BaseObservable {

    private static List<Resources.ResourceValue> icons;

    private Component component;

    // 图标
    @Bindable
    public Drawable icon;
    //标题
    @Bindable
    public String title;
    //作者
    @Bindable
    public String author;
    //描述
    @Bindable
    public String detail;

    //星星数
    private int stars;

    public ComponentInfo(Context context, Component component) {
        if (icons == null) {
            icons = new ArrayList<>(com.eazy.uibase.demo.resources.Drawables.icons(context).values());
        }
        this.component = component;
        int res = component.icon();
        if (res == 0 && !icons.isEmpty())
            res = icons.remove(0).getResId();
        icon = res == 0 ? null : Drawables.getDrawable(context, res);
        title = getText(context, component.title()).toString();
        Author annotation = component.getClass().getAnnotation(Author.class);
        author =  annotation == null ? "" : annotation.value();
        detail = context.getText(component.description()).toString();
    }

    public Component getComponent() {
        return component;
    }

    public int id() {
        return component.id();
    }

    @Bindable
    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
        notifyPropertyChanged(BR.stars);
    }

    private static CharSequence getText(Context context, int id) {
        if (id == 0)
            return "";
        return context.getText(id);
    }

}
