package ru.memebattle.repository

import ru.memebattle.model.RateuserModel

interface RateusersRepository {
    suspend fun add(userId: Long, userName: String)
    suspend fun getAll(): List<RateuserModel>
}