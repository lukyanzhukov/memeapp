package ru.memebattle.common.dto.game

import kotlinx.serialization.Serializable
import ru.memebattle.common.GameMode

@Serializable
data class MemeRequest(
    val number: Int,
    val gameMode: GameMode
)