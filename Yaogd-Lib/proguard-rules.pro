# 不优化（否则全部类都没有引用而被过滤掉）
-dontoptimize
# Android不需要preverify，去掉这一步可加快混淆速度
-dontpreverify
# 缺少运行时则会把jdk中的方法或者类去掉
-libraryjars  <java.home>/lib/rt.jar
-libraryjars  D:\android\android-sdk\platforms\android-22\android.jar

# 混淆时不使用大小写混合，混淆后的类名为小写
-dontusemixedcaseclassnames
# 指定不去忽略非公共的库的类
-dontskipnonpubliclibraryclasses
# 指定不去忽略非公共的库的类的成员
-dontskipnonpubliclibraryclassmembers

# 避免混淆注解、异常、内部类、泛型、废弃的方法、闭包方法、源文件、行号
-keepattributes *Annotation*,Exceptions,InnerClasses,Signature,Deprecated,EnclosingMethod,SourceFile,LineNumberTable

# 忽略警告
-dontwarn java.**,android.**
-dontwarn com.yaogd.lib.**
# 生成映射文件
-verbose
-printmapping out.map

# 对于R（资源）下的所有类及其方法，都不能被混淆
-keep class **.R$* { *;}
# 保留对外类的中的对外方法
-keep public class * {
    public protected *;
}

-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.v4.** {*;}
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

-keep public class * extends android.view.View {
      public <init>(android.content.Context);
      public <init>(android.content.Context, android.util.AttributeSet);
      public <init>(android.content.Context, android.util.AttributeSet, int);
      public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.content.Context {
    public void *(android.view.View);
    public void *(android.view.MenuItem);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keepclasseswithmembernames class * {
    native <methods>;
}
