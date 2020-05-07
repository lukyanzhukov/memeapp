package client.common.feature.splash

sealed class SplashState {
    object Progress: SplashState()
    object Error: SplashState()
}