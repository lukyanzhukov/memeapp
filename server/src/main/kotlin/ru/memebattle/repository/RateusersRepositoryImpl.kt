package ru.memebattle.repository

import org.jetbrains.exposed.sql.*
import ru.memebattle.common.GameMode
import ru.memebattle.db.data.rating.Rateusers
import ru.memebattle.db.data.rating.toRateUser
import ru.memebattle.db.dbQuery
import ru.memebattle.model.RateuserModel

class RateusersRepositoryImpl : RateusersRepository {
    override suspend fun add(
        userId: Long,
        userName: String,
        gameMode: GameMode
    ) {
        rateByMode(userId, userName, gameMode)
        rateByMode(userId, userName, GameMode.ALL)
    }

    override suspend fun getAll(): List<RateuserModel> =
        dbQuery {
            Rateusers.selectAll().map { it.toRateUser() }.sortedBy { it.likes }.reversed()
        }

    private suspend fun rateByMode(
        userId: Long,
        userName: String,
        gameMode: GameMode
    ) {
        dbQuery {
            val rateUser = Rateusers.select {
                Rateusers.id eq userId
            }.map {
                it.toRateUser()
            }.filter {
                it.mode == gameMode
            }.singleOrNull()
            if (rateUser == null) {
                Rateusers.insert {
                    it[id] = userId
                    it[name] = userName
                    it[likes] = 0
                }
            } else {
                Rateusers.update({ Rateusers.id eq userId }) {
                    it[likes] = rateUser.likes + 1
                }
            }
        }
    }
}