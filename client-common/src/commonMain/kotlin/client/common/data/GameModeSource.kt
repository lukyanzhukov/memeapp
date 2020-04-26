package client.common.data

import ru.memebattle.common.GameMode

interface GameModeSource {
    fun isGameModeUsed(gameMode: GameMode): Boolean
    fun setGameModeUsed(gameMode: GameMode)
}

class SettingsGameModeSource(val settings: Settings) : GameModeSource {
    override fun isGameModeUsed(gameMode: GameMode) = settings.getBoolean(gameMode.name) ?: false
    override fun setGameModeUsed(gameMode: GameMode) {
        settings.setBoolean(gameMode.name, true)
    }
}