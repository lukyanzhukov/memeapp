package ru.memebattle.common.dto.game

import kotlinx.serialization.Serializable

@Serializable
data class MemeRequest(
    val number: Int,
    val gameMode: String
)