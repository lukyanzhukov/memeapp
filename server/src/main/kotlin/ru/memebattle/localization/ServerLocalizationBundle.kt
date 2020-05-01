package ru.memebattle.localization

import com.ibm.icu.text.PluralRules
import java.util.*
import kotlin.reflect.jvm.jvmName

class ServerLocalizationBundle : ListResourceBundle() {

    override fun getContents(): Array<Array<Any>> = arrayOf(
        ServerLocalization.NO_LIKES to "Нет лайков",
        ServerLocalization.ONE_LIKES to "%d лайк",
        ServerLocalization.FEW_LIKES to "%d лайка",
        ServerLocalization.OTHER_LIKES to "%d лайков"
    )
}

fun getLikes(likes: Int, language: String?, country: String?): String {
    val locale = if (country == null && language != null) {
        Locale(language)
    } else if (language == null) {
        Locale("RU")
    } else {
        Locale(language, country)
    }

    val bundle = ResourceBundle
        .getBundle(ServerLocalizationBundle::class.jvmName, locale)

    if (likes == 0) {
        return bundle.getString(ServerLocalization.NO_LIKES)
    }

    val plurals = PluralRules.forLocale(locale)

    val plural = plurals.select(likes.toDouble())
    return when (plural) {
        PluralRules.KEYWORD_ONE -> bundle.getString(ServerLocalization.ONE_LIKES)
        PluralRules.KEYWORD_FEW -> bundle.getString(ServerLocalization.FEW_LIKES)
        else -> bundle.getString(ServerLocalization.OTHER_LIKES)
    }.format(likes)
}