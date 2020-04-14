package client.common.feature.splash

sealed class SplashNavigation {
    object ToMain: SplashNavigation()
    object ToAuth: SplashNavigation()
}