# Keep data classes for Room
-keepclassmembers class * {
    @androidx.room.Entity *;
    @androidx.room.Dao *;
    @androidx.room.Database *;
}

# Keep ViewBinding generated classes
-keep class com.calculator.databinding.** { *; }

# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }

# Coroutines
-keepclassmembers class kotlinx.coroutines.** { *; }