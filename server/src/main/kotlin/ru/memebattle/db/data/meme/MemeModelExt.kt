package ru.memebattle.db.data.meme

import org.jetbrains.exposed.sql.ResultRow
import ru.memebattle.common.dto.game.MemeModel

fun ResultRow.toMeme() = MemeModel(
    url = this[Memes.url],
    id = this[Memes.id],
    mode = this[Memes.mode]
)