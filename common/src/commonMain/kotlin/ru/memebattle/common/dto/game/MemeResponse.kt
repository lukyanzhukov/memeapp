package ru.memebattle.common.dto.game

import kotlinx.serialization.Serializable
import ru.memebattle.common.GameMode

@Serializable
data class MemeResponse(
    val state: GameState,
    val memes: List<String>,
    val likes: List<Int>,
    val timeEnd: Long,
    val gameMode: GameMode
)