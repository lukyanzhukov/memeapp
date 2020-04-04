package ru.memebattle.repository

import ru.memebattle.model.MemeModel

interface MemeRepository {
    suspend fun save(meme: MemeModel)
    suspend fun getAll(): List<MemeModel>
    suspend fun removeAll()
}