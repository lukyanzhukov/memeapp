package ru.memebattle.repository

import ru.memebattle.model.RateuserModel

interface RateusersRepository {
    suspend fun add(
        userId: Long,
        userName: String,
        gameMode: String
    )
    suspend fun getByMode(gameMode: String): List<RateuserModel>
}