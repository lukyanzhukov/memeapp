package ru.memebattle.db.data.rating

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Rateusers : Table() {
    val id: Column<Long> = Rateusers.long("id")
    val name: Column<String> = Rateusers.varchar("name", 100)
    val likes: Column<Long> = Rateusers.long("likes")
    val gmode: Column<String> = Rateusers.varchar("gmode", 100)
}