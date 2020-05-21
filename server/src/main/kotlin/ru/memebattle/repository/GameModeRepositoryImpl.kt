package ru.memebattle.repository

import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import ru.memebattle.common.dto.game.GameModeModel
import ru.memebattle.db.data.mode.GameModes
import ru.memebattle.db.data.mode.toGameMode
import ru.memebattle.db.dbQuery

class GameModeRepositoryImpl : GameModeRepository {

    override suspend fun add(gameMode: GameModeModel) {
        dbQuery {
            GameModes.insert { insertStatement ->
                insertStatement[imageUrl] = gameMode.imageUrl
                insertStatement[groupIds] = gameMode.groupIds
                insertStatement[name] = gameMode.name
                insertStatement[description] = gameMode.description
            }
        }
    }

    override suspend fun getAll(): List<GameModeModel> =
        dbQuery {
            GameModes.selectAll().map { it.toGameMode() }
        }

    override suspend fun delete(id: Long) {
        dbQuery {
            GameModes.deleteWhere {
                GameModes.id.eq(id)
            }
        }
    }

    override suspend fun update(gameMode: GameModeModel) {
        dbQuery {
            GameModes.update({GameModes.id.eq(gameMode.id)}) { gameModes ->
                gameModes[imageUrl] = gameMode.imageUrl
                gameModes[name] = gameMode.name
                gameModes[description] = gameMode.description
                gameModes[groupIds] = gameMode.groupIds
            }
        }
    }
}