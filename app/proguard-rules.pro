###############################
# ArcLogbook ProGuard / R8 Rules (hardened)
###############################

# Keep Room entities (annotations stripped cause runtime reflection by Room schema)
-keep class androidx.room.paging.** { *; }
-keep class com.arclogbook.data.** annotated with androidx.room.Entity
-keep @androidx.room.Dao class com.arclogbook.data.** { *; }
-keep class com.arclogbook.data.** extends java.lang.Enum { *; }

# Keep type adapters / Kotlin serialization (if used later)
-keep class kotlinx.serialization.** { *; }
-keep @kotlinx.serialization.Serializable class ** { *; }

# Hilt / Dagger generated code
-keep class dagger.hilt.internal.generated.** { *; }
-keep class * implements dagger.hilt.internal.GeneratedComponent { *; }
-keep class com.arclogbook.**_Factory { *; }
-keep class com.arclogbook.**_MembersInjector { *; }

# Microsoft Graph models often use reflection
-keep class com.microsoft.graph.** { *; }
-dontwarn com.microsoft.graph.**

# Retrofit / OkHttp / Gson
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature, InnerClasses, EnclosingMethod, RuntimeVisibleAnnotations

# Coroutines debug metadata (safe to strip but keep state machine minimal info)
-keepclassmembers class kotlinx.coroutines.DebugStringsKt { *; }

# Keep Application & MainActivity entry points
-keep class com.arclogbook.MainActivity { *; }
-keep class com.arclogbook.ArcLogbookApp { *; }

# Obfuscate ViewModels, but keep public no-arg constructors used by Hilt (factories retained above)
-keepnames class com.arclogbook.viewmodel.**

# Suppress warnings for TFLite optional delegates
-dontwarn org.tensorflow.lite.**

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

# Strip debug assertions
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    public static void checkParameterIsNotNull(...);
}

# Keep Lottie animation classes
-keep class com.airbnb.lottie.** { *; }

# Prevent removal of Compose runtime (already generally safe but explicit)
-keep class androidx.compose.runtime.** { *; }

# Optimize aggressively
-optimizations !code/simplification/arithmetic,!code/allocation/variable
