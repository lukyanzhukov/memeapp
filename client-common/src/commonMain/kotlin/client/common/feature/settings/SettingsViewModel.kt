package client.common.feature.settings

import client.common.data.LoginSource
import client.common.data.TokenSource

class SettingsViewModel(
    private val loginSource: LoginSource,
    private val tokenSource: TokenSource
) {

    fun logout() {
        loginSource.login = null
        tokenSource.token = null
    }
}