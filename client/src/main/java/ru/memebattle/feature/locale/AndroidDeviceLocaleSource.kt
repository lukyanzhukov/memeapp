package ru.memebattle.feature.locale

import android.content.res.Resources
import android.os.Build
import client.common.feature.localization.DeviceLocaleSource
import java.util.*

object AndroidDeviceLocaleSource : DeviceLocaleSource {

    override fun getLanguage(): String =
        deviceLocaleCompat.language

    override fun getCountry(): String =
        deviceLocaleCompat.country
}

@Suppress("DEPRECATION")
private val deviceLocaleCompat: Locale
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Resources.getSystem().configuration.locales[0]
        } else {
            Resources.getSystem().configuration.locale
        }
    }