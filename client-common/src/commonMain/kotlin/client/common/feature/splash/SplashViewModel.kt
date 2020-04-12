package client.common.feature.splash

import client.common.data.TokenSource

class SplashViewModel(private val tokenSource: TokenSource) {

    fun getRoute(): SplashNavigation =
        if (tokenSource.token != null) {
            SplashNavigation.ToMain
        } else {
            SplashNavigation.ToAuth
        }
}