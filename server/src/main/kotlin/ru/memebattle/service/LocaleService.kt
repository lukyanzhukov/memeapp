package ru.memebattle.service

import ru.memebattle.common.feature.localization.Localization
import ru.memebattle.localization.MemeBattleLocalization
import java.util.*
import kotlin.reflect.jvm.jvmName

class LocaleService {

    fun getLocale(language: String, country: String?): Map<Localization, String> {

        val locale = if (country == null) {
            Locale(language)
        } else {
            Locale(language, country)
        }

        val bundle = ResourceBundle
            .getBundle(MemeBattleLocalization::class.jvmName, locale)

        return bundle.keySet().map {
            Localization.valueOf(it) to bundle.getString(it)
        }.toMap()
    }
}