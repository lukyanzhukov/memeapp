package ru.memebattle.repository

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
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

    override suspend fun getByMode(gameMode: GameMode): List<RateuserModel> =
        dbQuery {
            Rateusers.selectAll()
                .map {
                    it.toRateUser()
                }
                .filter {
                    it.mode == gameMode
                }.sortedBy {
                    it.likes
                }.reversed()
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
                    it[gmode] = gameMode.name
                }
            } else {
                Rateusers.update({ Rateusers.id eq userId }) {
                    it[likes] = rateUser.likes + 1
                }
            }
        }
    }
}