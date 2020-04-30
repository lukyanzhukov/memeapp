package client.common.feature.settings

import client.common.data.LoginSource
import client.common.data.TokenSource
import client.common.presentation.ViewModel

class SettingsViewModel(
    private val loginSource: LoginSource,
    private val tokenSource: TokenSource
) : ViewModel() {

    fun isSignedIn() = tokenSource.token != null

    fun logout() {
        loginSource.login = null
        tokenSource.token = null
    }
}