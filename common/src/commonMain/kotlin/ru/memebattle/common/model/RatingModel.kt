package ru.memebattle.common.model

import kotlinx.serialization.Serializable

@Serializable
data class RatingModel(
    val playerName: String,
    val score: Long,
    val place: Long
)