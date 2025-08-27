# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep all generated protobuf classes completely
-keep class com.srizan.technonextcodingassessment.datastore.** { *; }

# Keep all protobuf runtime classes
-keep class com.google.protobuf.** { *; }
-dontwarn com.google.protobuf.**

# Keep protobuf generated message classes and their fields
-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite {
    <fields>;
    <methods>;
}

# Keep protobuf builders
-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite$Builder {
    <methods>;
}

# Specifically keep the AppsProtoPrefs class that's causing issues
-keep class com.srizan.technonextcodingassessment.datastore.AppsProtoPrefs { *; }
-keep class com.srizan.technonextcodingassessment.datastore.AppsProtoPrefs$Builder { *; }

# Keep DataStore serializers
-keep class * extends androidx.datastore.core.Serializer { *; }

# Keep all fields that end with underscore (protobuf naming convention)
-keepclassmembers class * {
    *** *_;
}