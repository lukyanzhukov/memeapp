package client.common.feature.memebattle

import ru.memebattle.common.dto.game.MemeResponse

sealed class MemeBattleState {
    data class Meme(val memeResponse: MemeResponse): MemeBattleState()
    object Error: MemeBattleState()
    object Progress: MemeBattleState()
}