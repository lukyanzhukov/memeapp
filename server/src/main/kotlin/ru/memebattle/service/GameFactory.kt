package ru.memebattle.service

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import ru.memebattle.common.dto.game.MemeRequest
import ru.memebattle.common.dto.game.MemeResponse
import ru.memebattle.model.UserModel
import ru.memebattle.repository.GameModeRepository
import ru.memebattle.repository.MemeRepository
import ru.memebattle.repository.RateusersRepository

class GameFactory(
    private val sendResponse: SendChannel<MemeResponse>,
    private val memeRepository: MemeRepository,
    private val rateusersRepository: RateusersRepository,
    private val gameModeRepository: GameModeRepository
) {
    private val mapOfMemeServices = mutableMapOf<String, MemeService>()

    init {
        GlobalScope.launch {
            createMemeServices()
        }
    }

    private suspend fun createMemeServices() {
        gameModeRepository.getAll().forEach { gameMode ->
            mapOfMemeServices[gameMode.name] =
                MemeService(sendResponse, memeRepository, rateusersRepository, gameMode.name)
        }
    }

    suspend fun rateMeme(memeRequest: MemeRequest, user: UserModel?) {
        mapOfMemeServices[memeRequest.gameMode]?.rateMeme(memeRequest.number, user)
    }

    suspend fun getStates(): List<MemeResponse> =
        mapOfMemeServices.map {
            it.value.getState()
        }

    suspend fun getAllMemes() = memeRepository.getAll()

    suspend fun getMemesByMode(mode: String) = memeRepository.getByMode(mode)

    suspend fun restartGames() {
        mapOfMemeServices.map {
            it.value.restart()
        }
    }
}