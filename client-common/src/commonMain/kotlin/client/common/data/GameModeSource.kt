package client.common.data

interface GameModeSource {
    fun isGameModeUsed(gameMode: String): Boolean
    fun setGameModeUsed(gameMode: String)
}

class SettingsGameModeSource(val settings: Settings) : GameModeSource {
    override fun isGameModeUsed(gameMode: String) = settings.getBoolean(gameMode) ?: false
    override fun setGameModeUsed(gameMode: String) {
        settings.setBoolean(gameMode, true)
    }
}