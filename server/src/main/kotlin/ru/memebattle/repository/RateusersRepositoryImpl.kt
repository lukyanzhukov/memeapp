package ru.memebattle.repository

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import ru.memebattle.db.data.rating.Rateusers
import ru.memebattle.db.data.rating.toRateUser
import ru.memebattle.db.dbQuery
import ru.memebattle.model.RateuserModel

class RateusersRepositoryImpl : RateusersRepository {
    override suspend fun add(userId: Long, userName: String) {
        dbQuery {
            val rateUser = Rateusers.select { Rateusers.id eq userId }.singleOrNull()?.toRateUser()
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

    override suspend fun getAll(): List<RateuserModel> =
        dbQuery {
            Rateusers.selectAll().map { it.toRateUser() }
        }
}