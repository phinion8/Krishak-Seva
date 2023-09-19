package com.phinion.planthelix.utils

import android.Manifest
import android.os.Build
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