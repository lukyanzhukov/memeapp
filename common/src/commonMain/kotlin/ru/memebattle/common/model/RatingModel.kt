package ru.memebattle.common.model

import kotlinx.serialization.Serializable
import ru.memebattle.common.GameMode

@Serializable
data class RatingModel(
    val playerName: String,
    val score: Long,
    val place: Long,
    val mode: GameMode
)