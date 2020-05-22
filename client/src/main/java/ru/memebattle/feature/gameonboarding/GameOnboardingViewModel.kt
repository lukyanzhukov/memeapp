package ru.memebattle.feature.gameonboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import client.common.presentation.PlatformSingleLiveEvent

class GameOnboardingViewModel : ViewModel() {

    private val _closeDialogEvent = PlatformSingleLiveEvent<Unit>()
    val closeDialogEvent: LiveData<Unit>
        get() = _closeDialogEvent

    fun onCloseDialog() {
        _closeDialogEvent.call()
    }
}