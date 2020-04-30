package ru.memebattle.service

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.memebattle.common.GameMode
import ru.memebattle.common.dto.game.GameState
import ru.memebattle.common.dto.game.MemeResponse
import ru.memebattle.model.MemeModel
import ru.memebattle.model.UserModel
import ru.memebattle.repository.MemeRepository
import ru.memebattle.repository.RateusersRepository
import java.time.Instant

class MemeService(
    private val sendResponse: SendChannel<MemeResponse>,
    private val memeRepository: MemeRepository,
    private val rateusersRepository: RateusersRepository,
    private val gameMode: GameMode
) {

    private var currentMemes: List<String> = emptyList()
    private var currentLikes: MutableList<Int> = mutableListOf(
        0, 0
    )
    private var firstLikes: MutableList<UserModel> = mutableListOf()
    private var secondLikes: MutableList<UserModel> = mutableListOf()
    private var state: GameState = GameState.START
    private var endTime = 0L
    private val mutex = Mutex()

    init {
        GlobalScope.launch {
            startRound()
        }
    }

    suspend fun getState() = MemeResponse(state, currentMemes, currentLikes, endTime, gameMode)

    suspend fun rateMeme(memeIndex: Int, user: UserModel?): MemeResponse =
        mutex.withLock {
            currentLikes[memeIndex] = currentLikes[memeIndex].inc()
            if (memeIndex == 0 && user != null) {
                firstLikes.add(user)
            }
            if (memeIndex == 1 && user != null) {
                secondLikes.add(user)
            }
            getState()
        }

    private suspend fun startRound() {

        withContext(Dispatchers.Default) {

            while (true) {
                val photos = getMemeModels().map { it.url }

                if (photos.isEmpty()) {
                    delay(10000)
                }

                val pairs = mutex.withLock {
                    val pairs: MutableList<Pair<String, String>> = mutableListOf()
                    for (s in 0..photos.size step 2) {
                        if (s.inc() <= photos.lastIndex) {
                            pairs.add(photos[s] to photos[s.inc()])
                        }
                    }
                    pairs
                }

                pairs.forEach {

                    mutex.withLock {
                        state = GameState.MEMES

                        currentMemes = listOf(it.first, it.second)

                        endTime = Instant.now().toEpochMilli() + 15000

                        sendResponse.send(getState())
                    }

                    delay(15000)

                    mutex.withLock {
                        state = GameState.RESULT

                        endTime = Instant.now().toEpochMilli() + 5000

                        sendResponse.send(getState())

                        if (currentLikes[0] > currentLikes[1]) {
                            firstLikes.forEach {
                                rateusersRepository.add(it.id, it.username)
                            }
                        }

                        if (currentLikes[1] > currentLikes[0]) {
                            secondLikes.forEach {
                                rateusersRepository.add(it.id, it.username)
                            }
                        }
                    }

                    delay(5000)

                    mutex.withLock {
                        currentLikes = mutableListOf(0, 0)
                        firstLikes = mutableListOf()
                        secondLikes = mutableListOf()
                    }
                }
            }
        }
    }

    private suspend fun getMemeModels(): List<MemeModel> =
        withContext(Dispatchers.IO) {
            memeRepository.getByMode(gameMode)
        }
}