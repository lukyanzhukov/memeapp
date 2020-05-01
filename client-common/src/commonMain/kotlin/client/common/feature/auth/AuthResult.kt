package client.common.feature.auth


sealed class AuthResult {
    object Success : AuthResult()
    sealed class Fail : AuthResult() {
        data class EmptyPassword(val message: String) : Fail()
        data class InvalidPassword(val message: String) : Fail()
        data class InvalidLogin(val message: String) : Fail()
        data class UserNotFound(val message: String) : Fail()
        data class UserAlreadyRegistered(val message: String) : Fail()
        data class NetworkError(val message: String) : Fail()
    }
}