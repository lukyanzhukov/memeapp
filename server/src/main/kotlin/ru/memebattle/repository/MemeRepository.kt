package ru.memebattle.repository

import ru.memebattle.common.dto.game.MemeModel

interface MemeRepository {
    suspend fun save(meme: MemeModel)
    suspend fun getAll(): List<MemeModel>
    suspend fun getByMode(mode: String): List<MemeModel>
    suspend fun removeAll()
}