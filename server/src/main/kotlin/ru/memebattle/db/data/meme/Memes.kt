package ru.memebattle.db.data.meme

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Memes : Table() {
    val id: Column<Long> = Memes.long("id").autoIncrement().primaryKey()
    val url: Column<String> = Memes.varchar("url", 150)
}