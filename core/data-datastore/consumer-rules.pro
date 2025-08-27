# Consumer ProGuard rules for DataStore module

# Keep all protobuf generated classes and their members
-keep class com.srizan.technonextcodingassessment.datastore.** { *; }

# Keep protobuf classes and their fields/methods
-keep class * extends com.google.protobuf.GeneratedMessageLite { *; }

# Keep protobuf builders
-keep class * extends com.google.protobuf.GeneratedMessageLite$Builder { *; }

# DataStore serializers must be kept
-keep class * extends androidx.datastore.core.Serializer { *; }

# Keep reflection-accessed fields
-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite {
    <fields>;
    <methods>;
}

# Keep protobuf runtime
-dontwarn com.google.protobuf.**
-keep class com.google.protobuf.** { *; }
