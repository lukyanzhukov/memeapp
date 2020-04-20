package ru.memebattle.common.feature.auth

object AuthValidator {
    /**
     * Пароль (Строчные и прописные латинские буквы, цифры).
     */
    private const val PASSWORD_REGEX = """^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\s).*${'$'}"""
    /**
     * Имя пользователя (с ограничением 2-20 символов, которыми могут быть буквы и цифры,
     * первый символ обязательно буква)
     */
    private const val LOGIN_REGEX = """^[a-zA-Z][a-zA-Z0-9-_\.]{1,20}${'$'}"""

    private val passwordRegex by lazy(LazyThreadSafetyMode.NONE) {
        PASSWORD_REGEX.toRegex()
    }
    private val loginRegex by lazy(LazyThreadSafetyMode.NONE) {
        LOGIN_REGEX.toRegex()
    }

    fun isPasswordValid(field: String): Boolean = field.matches(passwordRegex)

    fun isLoginValid(field: String): Boolean = field.matches(loginRegex)
}