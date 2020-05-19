package ru.memebattle.common.dto.game

import kotlinx.serialization.Serializable

@Serializable
data class MemeResponse(
    val state: GameState,
    val memes: List<MemeModel>,
    val likes: List<Int>,
    val timeEnd: Long,
    val gameMode: String,
    val firstLikesText: String? = null,
    val secondLikesText: String? = null
)