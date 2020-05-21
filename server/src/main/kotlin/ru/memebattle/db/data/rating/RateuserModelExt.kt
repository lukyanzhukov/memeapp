package ru.memebattle.db.data.rating

import org.jetbrains.exposed.sql.ResultRow
import ru.memebattle.model.RateuserModel

fun ResultRow.toRateUser() = RateuserModel(
    likes = this[Rateusers.likes],
    name = this[Rateusers.name],
    id = this[Rateusers.id],
    mode = this[Rateusers.gmode]
)