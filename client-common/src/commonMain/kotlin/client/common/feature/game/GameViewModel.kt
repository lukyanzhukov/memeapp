package client.common.feature.game

import client.common.data.GameModeSource
import client.common.presentation.ViewModel

class GameViewModel(
    private val gameSource: GameModeSource
) : ViewModel() {
    fun isGameModeUsed(gameMode: String): Boolean {
        val isUsed = gameSource.isGameModeUsed(gameMode)
        gameSource.setGameModeUsed(gameMode)
        return isUsed
    }
}