package ru.memebattle.feature.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import client.common.presentation.PlatformSingleLiveEvent

class OnboardingViewModel : ViewModel() {

    private val _closeDialogEvent = PlatformSingleLiveEvent<Unit>()
    val closeDialogEvent: LiveData<Unit>
        get() = _closeDialogEvent

    fun onCloseDialog() {
        _closeDialogEvent.call()
    }
}