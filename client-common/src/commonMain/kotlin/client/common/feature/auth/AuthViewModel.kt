package client.common.feature.auth

import client.common.data.LoginSource
import client.common.data.TokenSource
import client.common.data.signIn
import client.common.data.signUp
import client.common.feature.localization.LocalizationDelegate
import client.common.presentation.*
import io.ktor.client.HttpClient
import io.ktor.client.features.ResponseException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.launch
import ru.memebattle.common.dto.AuthenticationRequestDto
import ru.memebattle.common.feature.localization.Localization

class AuthViewModel(
    private val client: HttpClient,
    private val tokenSource: TokenSource,
    private val loginSource: LoginSource,
    private val localizationDelegate: LocalizationDelegate
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _authResult = SingleLiveEvent<AuthResult>()
    val authResult: LiveData<AuthResult>
        get() = _authResult

    fun auth(login: String, password: String) {
        viewModelScope.launch {
            val locale = localizationDelegate.localeChannel.openSubscription().receive()

            if (login.isEmpty()) {
                _authResult.value = AuthResult.Fail.InvalidLogin(
                    locale.getValue(Localization.AUTH_LOGIN_NOT_VALID_FIELD_ERROR)
                )
            }
            if (password.isEmpty()) {
                _authResult.value = AuthResult.Fail.InvalidPassword(
                    locale.getValue(Localization.AUTH_PASSWORD_NOT_VALID_FIELD_ERROR)
                )
            }

            viewModelScope.launch {
                try {
                    _loading.value = true

                    val response = client.signIn(
                        AuthenticationRequestDto(
                            username = login,
                            password = password
                        )
                    )

                    tokenSource.token = response.token
                    loginSource.login = login

                    _authResult.value = AuthResult.Success
                } catch (exception: Exception) {
                    _authResult.value = handleError(exception, locale)
                } finally {
                    _loading.value = false
                }
            }

        }
    }

    fun register(login: String, password: String) {
        viewModelScope.launch {
            val locale = localizationDelegate.localeChannel.openSubscription().receive()

            if (login.isEmpty()) {
                _authResult.value = AuthResult.Fail.InvalidLogin(
                    locale.getValue(Localization.AUTH_LOGIN_NOT_VALID_FIELD_ERROR)
                )
            }
            if (password.isEmpty()) {
                _authResult.value = AuthResult.Fail.InvalidPassword(
                    locale.getValue(Localization.AUTH_PASSWORD_NOT_VALID_FIELD_ERROR)
                )
            }

            viewModelScope.launch {
                try {
                    _loading.value = true

                    val response = client.signUp(
                        AuthenticationRequestDto(
                            username = login,
                            password = password
                        )
                    )

                    tokenSource.token = response.token
                    loginSource.login = login

                    _authResult.value = AuthResult.Success
                } catch (exception: Exception) {
                    _authResult.value = handleError(exception, locale)
                } finally {
                    _loading.value = false
                }
            }
        }
    }

    private fun handleError(
        exception: Exception,
        locale: Map<Localization, String>
    ): AuthResult.Fail =
        when (exception) {
            is ResponseException -> {
                when (exception.response.status) {
                    HttpStatusCode.Forbidden -> AuthResult.Fail.InvalidPassword(
                        locale.getValue(Localization.AUTH_WRONG_PASSWORD_ERROR_MESSAGE)
                    )
                    HttpStatusCode.NotFound -> AuthResult.Fail.UserNotFound(
                        locale.getValue(Localization.AUTH_USER_NOT_REGISTERED_ERROR_MESSAGE)
                    )
                    HttpStatusCode.BadRequest -> AuthResult.Fail.UserAlreadyRegistered(
                        locale.getValue(Localization.AUTH_USER_EXIST_ERROR_MESSAGE)
                    )
                    else -> AuthResult.Fail.NetworkError(
                        locale.getValue(Localization.AUTH_NETWORK_ERROR_MESSAGE)
                    )
                }
            }
            else -> AuthResult.Fail.NetworkError(
                locale.getValue(Localization.AUTH_NETWORK_ERROR_MESSAGE)
            )
        }
}