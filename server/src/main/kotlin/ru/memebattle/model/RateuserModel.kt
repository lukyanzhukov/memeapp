package ru.memebattle.model

data class RateuserModel(
    val id: Long = 0,
    val name: String,
    val likes: Long = 0,
    val mode: String
)