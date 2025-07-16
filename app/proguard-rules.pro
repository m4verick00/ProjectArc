# Proguard rules for ArcLogbook
# Obfuscate sensitive classes
-keep class com.arclogbook.viewmodel.** { *; }
-keep class com.arclogbook.data.** { *; }
-keep class com.arclogbook.network.** { *; }
# Hide API keys and sensitive fields
-obfuscationdictionary dictionary.txt
# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}
