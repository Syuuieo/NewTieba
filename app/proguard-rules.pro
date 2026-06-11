# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.

# Keep Hilt
-keepattributes *Annotation*
-keepattributes RuntimeVisibleAnnotations
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel

# Keep Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *

# Keep Retrofit
-keepattributes Signature
-keepattributes Exceptions
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Keep OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }

# Keep Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.newtieba.**$$serializer { *; }
-keepclassmembers class com.newtieba.** {
    *** Companion;
}
-keepclasseswithmembers class com.newtieba.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep Protobuf
-keep class com.squareup.wire.** { *; }
-keep class * extends com.squareup.wire.Message { *; }

# Keep data classes
-keep class com.newtieba.domain.model.** { *; }
-keep class com.newtieba.common.model.** { *; }

# Keep Compose
-dontwarn androidx.compose.**
-keep class androidx.compose.** { *; }

# Keep Miuix
-keep class top.yukonga.miuix.** { *; }

# General
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
