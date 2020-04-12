package client.common.feature.rating

import ru.memebattle.common.model.RatingModel

sealed class RatingState {

    data class Success(val rating: List<RatingModel>): RatingState()
    object Fail: RatingState()
    object Progress: RatingState()
}