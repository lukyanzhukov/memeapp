package client.common.feature.game

import ru.memebattle.common.dto.game.GameModeModel
import ru.memebattle.common.model.RatingModel

sealed class GameState {

    data class Success(val modes: List<GameModeModel>): GameState()
    object Fail: GameState()
    object Progress: GameState()
}