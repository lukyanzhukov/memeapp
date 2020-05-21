package ru.memebattle.repository

import ru.memebattle.common.dto.game.GameModeModel

interface GameModeRepository {
    suspend fun add(gameMode: GameModeModel)
    suspend fun getAll(): List<GameModeModel>
    suspend fun delete(id: Long)
    suspend fun update(gameMode: GameModeModel)
}