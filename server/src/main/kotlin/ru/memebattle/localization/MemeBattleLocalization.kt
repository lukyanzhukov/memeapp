package ru.memebattle.localization

import ru.memebattle.common.feature.localization.Localization
import java.util.*

class MemeBattleLocalization : ListResourceBundle() {

    override fun getContents(): Array<Array<Any>> = arrayOf(
        Localization.AUTH_PASSWORD_NOT_VALID_FIELD_ERROR to "Пароль должен содержать строчные и прописные латинские буквы и цифры",
        Localization.AUTH_LOGIN_NOT_VALID_FIELD_ERROR to "Логин должен состоять из латинских букв или цифр.",
        Localization.AUTH_WRONG_LOGIN_ERROR_MESSAGE to "Неправильный логин",
        Localization.AUTH_WRONG_PASSWORD_ERROR_MESSAGE to "Неправильный пароль",
        Localization.AUTH_USER_NOT_REGISTERED_ERROR_MESSAGE to "Вы не зарегистрированы",
        Localization.AUTH_USER_EXIST_ERROR_MESSAGE to "Вы уже зарегистрированы",
        Localization.AUTH_NETWORK_ERROR_MESSAGE to "Проблемы с сетью",
        Localization.AUTH_LOGIN_HINT_TEXT to "Логин",
        Localization.AUTH_PASSWORD_HINT_TEXT to "Пароль",
        Localization.AUTH_SIGN_IN_BUTTON_TEXT to "Вход",
        Localization.AUTH_SIGN_UP_BUTTON_TEXT to "Регистрация",
        Localization.CHILL_GAME_ONBOARDING_TITLE to "Чилим",
        Localization.CHILL_GAME_ONBOARDING_TEXT to "В этом режиме ты можешь выбирать понравившиеся мемы не смотря на время.\n Только ты, наслаждение и мемы..\n\nТапни два раза, чтобы выбрать понравившуюся картинку",
        Localization.CLASSIC_GAME_ONBOARDING_TEXT to "У тебя есть всего 15 секунд на выбор самой смешной картинки из пары.\n Помоги смешным мемам выиграть эту битву и получи за это очки.\n\nТапни два раза, чтобы выбрать понравившуюся картинку",
        Localization.CLASSIC_GAME_ONBOARDING_TITLE to "В первый раз?",
        Localization.ONBOARDING_OK_BTN_TEXT to "Погнали",
        Localization.GAME_WAIT_NEXT_ROUND_TEXT to "Ждём начала следующего раунда...",
        Localization.GAME_SHARE_IMAGE_TEXT to """"Я нашёл этот мемасик в Memebattle, заходи поорём: https://play.google.com/store/apps/details?id=ru.memeapp """",
        Localization.GAME_SAVE_IMAGE_TEXT to "Сохранено как: %s",
        Localization.SETTINGS_LOGOUT_TEXT to "Выйти из аккаунта",
        Localization.SETTINGS_RATE_TEXT to "Оценить приложение",
        Localization.SETTINGS_SHARE_TEXT to "Поделиться приложением",
        Localization.SETTINGS_HASH_TAG_TEXT to "#memes_team",
        Localization.SETTINGS_SIGN_UP_BUTTON to "Зарегистрироваться",
        Localization.SETTINGS_SIGN_IN_BUTTON to "Войти",
        Localization.MAIN_MENU_MEMES to "Memes",
        Localization.MAIN_MENU_RATING to "Rating",
        Localization.MAIN_MENU_SETTINGS to "Settings",
        Localization.ERROR_LOADING_TEXT to "Ошибка загрузки",
        Localization.ERROR_LOADING_BUTTON_TEXT to "Повторить",
        Localization.FIRST_WIN_AUTH_TEXT to "Войти",
        Localization.FIRST_WIN_SKIP_TEXT to "В другой раз",
        Localization.FIRST_WIN_SECOND_TEXT to "Пройди авторизацию, чтобы мы могли начислять тебе призовые очки",
        Localization.FIRST_WIN_FIRST_TEXT to "Это твоя первая победа в MemeApp.",
        Localization.FIRST_WIN_TITLE to "Поздравляем!",
        Localization.SHARE_APP_TEXT to "Скачал мужик MemeBattle, а он ему как раз https://play.google.com/store/apps/details?id=ru.memeapp",
        Localization.SHARE_APP_TITLE to "Поделиться с друзьями",
        Localization.FATAL_ERROR_DIALOG_BTN_TEXT to "Ошибка интернет-соединения",
        Localization.FATAL_ERROR_DIALOG_TEXT to "Вернуться",
        Localization.SIGN_OUT_DIALOG_TEXT to
                """Ты точно хочешь 
                    |выйти из аккаунта?""".trimMargin(),
        Localization.SIGN_OUT_DIALOG_BUTTON to "Выйти"
    )
}

internal infix fun Enum<*>.to(any: Any): Array<Any> = arrayOf(this.name, any)

internal infix fun ResourceBundle.getString(enum: Enum<*>): String = getString(enum.name)