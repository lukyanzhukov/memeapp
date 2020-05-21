package ru.memebattle.db.data.mode

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object GameModes : Table() {
    val id: Column<Long> = GameModes.long("id").autoIncrement().primaryKey()
    val name: Column<String> = GameModes.varchar("name", 150)
    val imageUrl: Column<String> = GameModes.varchar("image_url", 150)
    val groupIds: Column<String> = GameModes.varchar("group_ids", 150)
    val description: Column<String> = GameModes.varchar("description", 150)
}