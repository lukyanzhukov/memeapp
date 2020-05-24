package ru.memebattle.feature.gameonboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import client.common.presentation.PlatformSingleLiveEvent
import ru.memebattle.feature.game.GameModeTab

class GameOnboardingViewModel : ViewModel() {

    private val _closeDialogEvent = PlatformSingleLiveEvent<GameModeTab>()
    val closeDialogEvent: LiveData<GameModeTab>
        get() = _closeDialogEvent

    fun onCloseDialog(mode: GameModeTab) {
        _closeDialogEvent.value = mode
    }
}