package client.common.feature.splash

import client.common.data.TokenSource
import client.common.presentation.ViewModel

class SplashViewModel(private val tokenSource: TokenSource) : ViewModel() {

    fun getRoute(): SplashNavigation =
        if (tokenSource.token != null) {
            SplashNavigation.ToMain
        } else {
            SplashNavigation.ToAuth
        }
}