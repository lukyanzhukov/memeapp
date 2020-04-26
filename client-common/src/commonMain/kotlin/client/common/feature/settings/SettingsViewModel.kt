package client.common.feature.settings

import client.common.data.LoginSource
import client.common.data.TokenSource
import client.common.presentation.ViewModel

class SettingsViewModel(
    private val loginSource: LoginSource,
    private val tokenSource: TokenSource
) : ViewModel() {

    fun logout() {
        loginSource.login = null
        tokenSource.token = null
    }
}