package com.phinion.planthelix.utils

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


object Utils {
    fun isSkip(permission: String, config: Boolean): Boolean {
        if (isAndroidQAndAbove() &&
            config && permission == Manifest.permission.READ_EXTERNAL_STORAGE
        ) {
            return true
        } else if (isAndroidQAndAbove().not() && config && permission == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
            return true
        }
        return false
    }
}

fun isAndroidQAndAbove(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
}

fun isAndroidRAndAbove(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
}
fun generateUniqueFileName(fileExtension: String): String {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val randomString = UUID.randomUUID().toString().replace("-", "").substring(0, 6)
    return "file_$timeStamp$randomString.$fileExtension"
}

fun setLocale(context: Context, language: String) {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val configuration = Configuration()
    configuration.locale = locale
    context.resources?.updateConfiguration(configuration, context.resources.displayMetrics)

    val editor: SharedPreferences.Editor? =
        context.getSharedPreferences("Settings", Context.MODE_PRIVATE)?.edit()
    editor?.putString("app_lang", language)
    editor?.apply()

}

fun loadLocale(context: Context){
    val prefs: SharedPreferences? = context.getSharedPreferences("Settings",
        Context.MODE_PRIVATE
    )
    val language =
        prefs?.getString("app_lang", "en")
    if (language != null) {
        setLocale(context, language)
    }
}

fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

//fun isLocationEnabled(context: Context): Boolean {
//    var locationMode = 0
//    val locationProviders: String
//    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//        locationMode = try {
//            Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
//        } catch (e: SettingNotFoundException) {
//            e.printStackTrace()
//            return false
//        }
//        locationMode != Settings.Secure.LOCATION_MODE_OFF
//    } else {
//        locationProviders = Settings.Secure.getString(
//            context.contentResolver,
//            Settings.Secure.LOCATION_PROVIDERS_ALLOWED
//        )
//        !TextUtils.isEmpty(locationProviders)
//    }
//}
