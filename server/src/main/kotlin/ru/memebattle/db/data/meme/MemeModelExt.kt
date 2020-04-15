package ru.memebattle.db.data.meme

import org.jetbrains.exposed.sql.ResultRow
import ru.memebattle.model.MemeModel

fun ResultRow.toMeme() = MemeModel(
    url = this[Memes.url],
    id = this[Memes.id],
    mode = this[Memes.mode]
)