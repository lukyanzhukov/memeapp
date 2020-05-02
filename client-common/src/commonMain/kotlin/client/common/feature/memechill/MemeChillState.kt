package client.common.feature.memechill

import ru.memebattle.common.dto.game.MemeModel

sealed class MemeChillState {
    object Loading : MemeChillState()
    data class SuccessMemePair(val memes: Pair<MemeModel, MemeModel>) : MemeChillState()
    data class Error(val error: Throwable) : MemeChillState()
}