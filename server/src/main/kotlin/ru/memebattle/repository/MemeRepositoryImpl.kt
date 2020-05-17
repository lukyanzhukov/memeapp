package ru.memebattle.repository

import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import ru.memebattle.common.GameMode
import ru.memebattle.db.data.meme.Memes
import ru.memebattle.db.data.meme.toMeme
import ru.memebattle.db.dbQuery
import ru.memebattle.common.dto.game.MemeModel

class MemeRepositoryImpl : MemeRepository {
    override suspend fun save(meme: MemeModel) {
        dbQuery {
            Memes.insert { insertStatement ->
                insertStatement[url] = meme.url
                insertStatement[mode] = meme.mode
                insertStatement[text] = meme.text
                insertStatement[source_id] = meme.sourceId
                insertStatement[source_url] = meme.sourceUrl
                insertStatement[likes] = meme.likes
            }
        }
    }

    override suspend fun getAll(): List<MemeModel> =
        dbQuery {
            Memes.selectAll().map { it.toMeme() }
        }

    override suspend fun getByMode(mode: GameMode): List<MemeModel> =
        dbQuery {
            Memes.select {
                Memes.mode.eq(mode.name)
            }.map { it.toMeme() }
        }

    override suspend fun removeAll() {
        dbQuery {
            Memes.deleteAll()
        }
    }
}