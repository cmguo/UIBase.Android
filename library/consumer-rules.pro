# 白天夜间模式需要保留的符号
-keep class com.xhb.uibase.daynight.DayNightViewInflater
-keepclassmembers class androidx.appcompat.content.res.AppCompatResources {
    ** sColorStateCaches;
}
-keepclassmembers class androidx.appcompat.app.AppCompatViewInflater {
    ** createViewFromTag(**, **, **);
}
# AutoService will handle these
# -keep class com.xhb.uibase.daynight.styleable.IStyleableSet
# -keep class * implements com.xhb.uibase.daynight.styleable.IStyleableSet

# View 派生类默认 keep
# -keep public class * extends android.view.View
