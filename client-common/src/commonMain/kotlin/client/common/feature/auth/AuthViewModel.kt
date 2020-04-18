package client.common.feature.auth

import client.common.data.LoginSource
import client.common.data.TokenSource
import client.common.data.signIn
import client.common.data.signUp
import client.common.presentation.*
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.launch
import ru.memebattle.common.dto.AuthenticationRequestDto

class AuthViewModel(
    private val client: HttpClient,
    private val tokenSource: TokenSource,
    private val loginSource: LoginSource
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _authResult = SingleLiveEvent<AuthResult>()
    val authResult: LiveData<AuthResult>
        get() = _authResult

    fun auth(login: String, password: String) {
        if (login.isEmpty()) {
            _authResult.value = AuthResult.Fail.InvalidLogin
        }
        if (password.isEmpty()) {
            _authResult.value = AuthResult.Fail.InvalidPassword
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
                _authResult.value = handleError(exception)
            } finally {
                _loading.value = false
            }
        }
    }

    fun register(login: String, password: String) {
        if (login.isEmpty()) {
            _authResult.value = AuthResult.Fail.InvalidLogin
        }
        if (password.isEmpty()) {
            _authResult.value = AuthResult.Fail.EmptyPassword
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
                _authResult.value = handleError(exception)
            } finally {
                _loading.value = false
            }
        }
    }

    fun isPasswordNotValid(field: String): Boolean {
        val regex = PASSWORD_REGEX.toRegex()
        return !field.matches(regex)
    }

    fun isLoginNotValid(field: String): Boolean {
        val regex = LOGIN_REGEX.toRegex()
        return !field.matches(regex)
    }

    private fun handleError(exception: Exception): AuthResult.Fail =
        when (exception) {
            is ClientRequestException -> {
                when (exception.response.status) {
                    HttpStatusCode.Forbidden -> AuthResult.Fail.InvalidPassword
                    HttpStatusCode.NotFound -> AuthResult.Fail.UserNotFound
                    HttpStatusCode.BadRequest -> AuthResult.Fail.UserAlreadyRegistered
                    else -> AuthResult.Fail.NetworkError
                }
            }
            else -> AuthResult.Fail.NetworkError
        }

    companion object {
        const val PASSWORD_REGEX = """^(?=.*[0-9])(?=.*[a-z])(?=\\S+${'$'}).{8,}${'$'}"""
        const val LOGIN_REGEX = """^(?=.*[a-z])(?=\\S+${'$'}).{4,}${'$'}"""
    }
}