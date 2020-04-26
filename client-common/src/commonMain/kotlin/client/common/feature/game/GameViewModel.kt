package client.common.feature.game

import client.common.data.GameModeSource
import client.common.presentation.ViewModel
import ru.memebattle.common.GameMode

class GameViewModel(
    private val gameSource: GameModeSource
) : ViewModel() {
    fun isGameModeUsed(gameMode: GameMode): Boolean {
        val isUsed = gameSource.isGameModeUsed(gameMode)
        gameSource.setGameModeUsed(gameMode)
        return isUsed
    }
}