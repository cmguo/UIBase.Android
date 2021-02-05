package com.xhb.uibase.demo.core;

public interface Component {
    // return string res id
    int group();
    // return drawable res id
    int icon();
    // return string res id
    int title();
    // return string res id
    int description();

    ViewStyles styles();
}
