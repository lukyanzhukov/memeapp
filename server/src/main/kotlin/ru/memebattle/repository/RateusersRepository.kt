package ru.memebattle.repository

import ru.memebattle.common.GameMode
import ru.memebattle.model.RateuserModel

interface RateusersRepository {
    suspend fun add(
        userId: Long,
        userName: String,
        gameMode: GameMode
    )
    suspend fun getByMode(gameMode: GameMode): List<RateuserModel>
}