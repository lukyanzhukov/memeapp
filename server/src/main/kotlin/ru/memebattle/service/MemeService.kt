package ru.memebattle.service

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.memebattle.common.dto.game.GameState
import ru.memebattle.common.dto.game.MemeResponse
import ru.memebattle.model.MemeModel
import ru.memebattle.repository.MemeRepository

class MemeService(
    private val sendResponse: SendChannel<MemeResponse>,
    private val memeRepository: MemeRepository
) {

    private var currentMemes: List<String> = emptyList()
    private var currentLikes: MutableList<Int> = mutableListOf(
        0, 0
    )
    private var state: GameState = GameState.START
    private val mutex = Mutex()

    init {
        GlobalScope.launch {
            startRound()
        }
    }

    suspend fun rateMeme(memeIndex: Int): MemeResponse =
        mutex.withLock {
            currentLikes[memeIndex] = currentLikes[memeIndex].inc()
            MemeResponse(state, currentMemes, currentLikes)
        }

    private suspend fun startRound() {

        withContext(Dispatchers.Default) {

            while (true) {
                val photos = getMemeModels().map { it.url }

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

                        sendResponse.send(MemeResponse(state, currentMemes, currentLikes))
                    }

                    delay(10000)

                    mutex.withLock {
                        state = GameState.RESULT

                        sendResponse.send(MemeResponse(state, currentMemes, currentLikes))
                    }

                    delay(5000)

                    mutex.withLock {
                        currentLikes = mutableListOf(0, 0)
                    }
                }
            }
        }
    }

    private suspend fun getMemeModels(): List<MemeModel> =
        withContext(Dispatchers.IO) {
            memeRepository.getAll()
        }
}