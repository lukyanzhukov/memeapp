package client.common.feature.auth


sealed class AuthResult {
    object Success: AuthResult()
    sealed class Fail: AuthResult() {
        object EmptyPassword: Fail()
        object InvalidPassword: Fail()
        object InvalidLogin: Fail()
        object UserNotFound: Fail()
        object UserAlreadyRegistered: Fail()
        object NetworkError: Fail()
    }
}