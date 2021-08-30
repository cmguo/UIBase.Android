# 按钮

按钮控件提供下列特性
- 增强样式：在系统按钮基础上，增加圆角尺寸、背景色、前景色，图标位置，图标尺寸等样式
- 标准样式：预置标准样式，分为类型、尺寸两个大类，两者可自由组合
- 自定义样式：可扩展定义样式集合，其中可以组合使用标准样式
- 特殊行为模式：图标变色，加载状态
- 内容样式：组合内容样式（文字、图标），可在集成控件中灵活定义按钮内容

# 基础使用方式
``` xml
<com.eazy.uibase.widget.ZButton
    android:id="@+id/button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="center"
    app:buttonType="primitive"
    app:buttonSize="small"
    app:buttonAppearance="@style/ZButton.Appearance.Action"
    app:content="@style/button_cancel"
    app:icon="@drawable/icon_tip"
    app:iconPosition="right"
    app:loadingText="。。。"
    app:loadingIcon="@drawable/icon_loading"
    app:loading="@{true}"
    android:text="按钮"/>
```

# 自定义按钮样式集合，然后在 app:buttonAppearance 中引用，也可以在布局定义中指定这些样式（如：cornerRadius）
``` xml
<style name="ZButton.Appearance.Action" parent="ZButton.Appearance.TextLink.Middle">
    <item name="android:textColor">@color/bluegrey_900_selected_disabled</item>
    <item name="android:minHeight">48dp</item>
    <item name="cornerRadius">8dp</item>
</style>

<style name="ZButton.Appearance.ListItem" parent="ZButton.Appearance.Middle">
    <item name="android:textColor">@color/bluegrey_700_disabled</item>
    <item name="backgroundColor">@color/transparent</item>
    <item name="iconPosition">right</item>
</style>
```

# 组合定义按钮内容
``` xml
<array name="button_icon_text">
    <item>@drawable/icon_exit</item>
    <item>文字</item>
</array>
```

# 文字按钮
这是（TextLink）按钮的定义，默认没有 padding
``` xml
<style name="ZButton.Appearance.TextLink">
    <item name="android:textColor">@color/blue_600_disabled</item>
    <item name="backgroundColor">@color/transparent_pressed_disabled</item>
    <item name="paddingX">0dp</item>
    <item name="paddingY">0dp</item>
    <item name="android:minHeight">0dp</item>
</style>
```
