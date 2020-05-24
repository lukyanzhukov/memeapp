package client.common.feature.rating

import ru.memebattle.common.dto.game.GameModeModel
import ru.memebattle.common.model.RatingModel

sealed class RatingState {

    data class Success(
        val rating: List<RatingModel>,
        val modes: List<GameModeModel>? = null
    ): RatingState()
    object Fail: RatingState()
    object Progress: RatingState()
}