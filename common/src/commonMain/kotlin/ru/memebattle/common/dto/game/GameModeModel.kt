package ru.memebattle.common.dto.game

import kotlinx.serialization.Serializable

@Serializable
data class GameModeModel(
    val id: Long = 0,
    val imageUrl: String,
    val name: String,
    val groupIds: String,
    val description: String
)