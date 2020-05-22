package client.common.feature.splash

sealed class NavState {
    object Onboarding : NavState()
    object Main : NavState()
}