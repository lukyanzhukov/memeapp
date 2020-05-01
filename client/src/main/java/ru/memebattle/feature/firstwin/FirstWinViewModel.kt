package ru.memebattle.feature.firstwin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import client.common.presentation.PlatformSingleLiveEvent

class FirstWinViewModel : ViewModel() {

    private val _authEvent = PlatformSingleLiveEvent<Unit>()
    val authEvent: LiveData<Unit>
        get() = _authEvent

    fun onAuthClicked() {
        _authEvent.call()
    }
}