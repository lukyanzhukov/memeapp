package client.common.feature.memebattle

import ru.memebattle.common.dto.game.MemeResponse

sealed class MemeBattleState {
    data class Meme(val memeResponse: MemeResponse): MemeBattleState()
    data class Error(val throwable: String?): MemeBattleState()
    object Progress: MemeBattleState()
}