package client.common.feature.game

import client.common.data.GameModeSource
import ru.memebattle.common.GameMode

class GameViewModel(
    private val gameSource: GameModeSource
) {
    fun isGameModeUsed(gameMode: GameMode): Boolean {
        val isUsed = gameSource.isGameModeUsed(gameMode)
        gameSource.setGameModeUsed(gameMode)
        return isUsed
    }
}