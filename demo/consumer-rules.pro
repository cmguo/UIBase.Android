# 对R文件下的所有类及其方法，都不能被混淆，演示会反射获取所有资源，此时需要有意义的名称
-keep class **.R$*
-keepclassmembers class **.R$* {
    *;
}
-keepclassmembers class **.BR {
    *;
}

# AutoService will handle these
# -keep class com.xhb.uibase.demo.core.Component
# -keep class * implements com.xhb.uibase.demo.core.Component

-keepclassmembers class * extends com.ustc.base.prop.PropertySet {
    static ** PROP_*;
}

-keepclassmembers class * extends androidx.databinding.ViewDataBinding {
    static ** inflate(**);
}


-keepclassmembers class * extends com.xhb.uibase.demo.core.ViewStyles {
    *;
}

-keepclassmembers class * extends com.xhb.uibase.demo.core.ViewModel {
    <init>(...);
    void set*(...);
    ** get();
}

-keepclassmembers class * extends com.xhb.uibase.demo.core.style.ComponentStyle {
    <init>(...);
}

-keep public enum * {
  **[] $VALUES;
  public *;
}

-keep class **.*Component
-keep class **.*Fragment
