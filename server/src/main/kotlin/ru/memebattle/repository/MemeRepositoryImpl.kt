package ru.memebattle.repository

import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import ru.memebattle.db.data.meme.Memes
import ru.memebattle.db.data.meme.toMeme
import ru.memebattle.db.dbQuery
import ru.memebattle.model.MemeModel

class MemeRepositoryImpl : MemeRepository {
    override suspend fun save(meme: MemeModel) {
        dbQuery {
            Memes.insert { insertStatement ->
                insertStatement[url] = meme.url
            }
        }
    }

    override suspend fun getAll(): List<MemeModel> =
        dbQuery {
            Memes.selectAll().map { it.toMeme() }
        }

    override suspend fun removeAll() {
        dbQuery {
            Memes.deleteAll()
        }
    }
}