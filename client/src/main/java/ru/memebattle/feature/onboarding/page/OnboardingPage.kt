package ru.memebattle.feature.onboarding.page

import ru.memebattle.R

/**
 * Модель страницы ViewPager'а онбординга.
 */
enum class OnboardingPage constructor(
    val titleRes: Int,
    val descriptionRes: Int,
    val backgroundRes: Int,
    val isLastPage: Boolean = false
) {

    FIRST_ONBOARDING_PAGE(
        R.string.first_page_title,
        R.string.first_page_description,
        R.drawable.ic_settings_logo
    ),
    SECOND_ONBOARDING_PAGE(
        R.string.second_page_title,
        R.string.second_page_description,
        R.drawable.second_page_banner
    ),
    THIRD_ONBOARDING_PAGE(
        R.string.third_page_title,
        R.string.third_page_description,
        R.drawable.third_page_banner
    ),
    FOURTH_ONBOARDING_PAGE(
        R.string.fourth_page_title,
        R.string.fourth_page_description,
        R.drawable.fourth_page_banner,
        true
    )
}
