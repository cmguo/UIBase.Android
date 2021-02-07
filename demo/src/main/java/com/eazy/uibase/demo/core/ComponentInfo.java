package com.eazy.uibase.demo.core;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.eazy.uibase.demo.core.annotation.Author;

//保存子行信息的类
public class ComponentInfo {

    private Component component;
    //头像,用于设置给ImageView。
    private Drawable icon;
    //标题
    private String title;
    //作者
    private String author;
    //星星数
    private int stars;
    //描述
    private String detail;

    public ComponentInfo(Context context, Component component) {
        this.component = component;
        icon = context.getDrawable(component.icon());
        title = getText(context, component.title()).toString();
        Author annotation = component.getClass().getAnnotation(Author.class);
        author =  annotation == null ? "" : annotation.value();
        detail = context.getText(component.description()).toString();
    }

    public ComponentInfo(Drawable icon, String title, String author, String detail) {
        this.icon = icon;
        this.title = title;
        this.author = author;
        this.detail = detail;
    }

    public Component getComponent() {
        return component;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getStars() {
        return stars;
    }

    public String getDetail() {
        return detail;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public static CharSequence getText(Context context, int id) {
        if (id == 0)
            return "";
        return context.getText(id);
    }

}
