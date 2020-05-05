package ru.memebattle.common.dto.game

import kotlinx.serialization.Serializable

@Serializable
data class MemeModel(
    val id: Long = 0,
    val url: String,
    val mode: String,
    val text: String,
    val sourceId: String,
    val sourceUrl: String
)