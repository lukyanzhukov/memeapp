package ru.memebattle.db.data.mode

import org.jetbrains.exposed.sql.ResultRow
import ru.memebattle.common.dto.game.GameModeModel
import ru.memebattle.common.dto.game.MemeModel

fun ResultRow.toGameMode() = GameModeModel(
    name = this[GameModes.name],
    id = this[GameModes.id],
    description = this[GameModes.description],
    groupIds = this[GameModes.groupIds],
    imageUrl = this[GameModes.imageUrl]
)