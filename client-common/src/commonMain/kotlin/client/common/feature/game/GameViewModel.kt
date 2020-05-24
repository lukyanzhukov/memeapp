package client.common.feature.game

import client.common.data.GameModeSource
import client.common.data.getGameModes
import client.common.presentation.LiveData
import client.common.presentation.MutableLiveData
import client.common.presentation.ViewModel
import client.common.presentation.viewModelScope
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch

class GameViewModel(
    private val gameSource: GameModeSource,
    private val httpClient: HttpClient
) : ViewModel() {

    private val _state = MutableLiveData<GameState>()
    val state: LiveData<GameState>
        get() = _state

    fun getModes() {
        viewModelScope.launch {
            try {
                _state.value = GameState.Progress
                val modes = httpClient.getGameModes()
                print(modes)
                _state.value = GameState.Success(modes)
            } catch (e: Exception) {
                _state.value = GameState.Fail
            }
        }
    }

    fun isGameModeUsed(gameMode: String): Boolean {
        val isUsed = gameSource.isGameModeUsed(gameMode)
        gameSource.setGameModeUsed(gameMode)
        return isUsed
    }
}