package ru.memebattle.service

import kotlinx.coroutines.channels.SendChannel
import ru.memebattle.common.GameMode
import ru.memebattle.common.dto.game.MemeRequest
import ru.memebattle.common.dto.game.MemeResponse
import ru.memebattle.model.UserModel
import ru.memebattle.repository.MemeRepository
import ru.memebattle.repository.RateusersRepository

class GameFactory(
    private val sendResponse: SendChannel<MemeResponse>,
    private val memeRepository: MemeRepository,
    private val rateusersRepository: RateusersRepository
) {
    private val mapOfMemeServices = mutableMapOf<GameMode, MemeService>()

    init {
        createMemeServices()
    }

    private fun createMemeServices() {
        GameMode.values().forEach {
            mapOfMemeServices[it] =
                MemeService(sendResponse, memeRepository, rateusersRepository, it)
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
}