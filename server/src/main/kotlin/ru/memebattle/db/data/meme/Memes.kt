package ru.memebattle.db.data.meme

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Memes : Table() {
    val id: Column<Long> = Memes.long("id").autoIncrement().primaryKey()
    val url: Column<String> = Memes.varchar("url", 150)
    val mode: Column<String> = Memes.varchar("mode", 50)
    val text: Column<String> = Memes.varchar("text", 150)
    val source_id: Column<String> = Memes.varchar("source_id", 50)
    val source_url: Column<String> = Memes.varchar("source_url", 150)
    val likes: Column<Int> = Memes.integer("likes")
}