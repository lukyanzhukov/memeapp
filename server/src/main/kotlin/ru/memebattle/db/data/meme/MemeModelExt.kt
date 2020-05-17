package ru.memebattle.db.data.meme

import org.jetbrains.exposed.sql.ResultRow
import ru.memebattle.common.dto.game.MemeModel

fun ResultRow.toMeme() = MemeModel(
    url = this[Memes.url],
    id = this[Memes.id],
    mode = this[Memes.mode],
    text = this[Memes.text],
    sourceId = this[Memes.source_id],
    sourceUrl = this[Memes.source_url],
    likes = this[Memes.likes]
)