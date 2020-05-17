package ru.memebattle.service

import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get
import kotlinx.coroutines.*
import ru.memebattle.common.GameMode
import ru.memebattle.common.dto.game.MemeModel
import ru.memebattle.model.vk.model.VKResponse
import ru.memebattle.model.vk.model.getMaxImage
import ru.memebattle.repository.MemeRepository

class ParserService(
    private val memeRepository: MemeRepository
) {

    init {
        GlobalScope.launch {
            startParser()
        }
    }

    private suspend fun startParser() {
        withContext(Dispatchers.Default) {
            while (true) {
                memeRepository.removeAll()
                val client = HttpClient(Apache)
                val modes = mapOf(
                    GameMode.CLASSIC to listOf("154306815"),
                    GameMode.SENIOR to listOf("169651616", "183402030"),
                    GameMode.ENGLISH to listOf("150550417", "120254617", "121642448", "154906069"),
                    GameMode.IT to listOf("174299957", "72495085", "80799846", "76746437"),
                    GameMode.SCIENCE to listOf("143120001", "176400729", "143031608"),
                    GameMode.STUDY to listOf("129368275"),
                    GameMode.WORK to listOf("124636304")
                )
                modes.forEach { mode ->
                    mode.value.forEach { groupId ->
                        val response =
                            client.get<String>("https://api.vk.com/method/wall.get?owner_id=-$groupId&access_token=2f0935dcc011e41fdd54052842b577d80739bbca71728837395626c0a00c4fa68e6e2ecedc412cb0d1a40&v=5.103&extended=1&count=40")
                        val vkResponse = Gson().fromJson(response, VKResponse::class.java)
                        val posts = vkResponse.response?.items?.filter {
                            it.markedAsAds != 1
                        }
                        val sourceId = vkResponse.response?.groups?.first()?.screenName ?: ""
                        val sourceUrl = if (sourceId == "") {
                            ""
                        } else {
                            "https://vk.com/${sourceId}"
                        }
                        posts?.forEach { post ->
                            val url = post.attachments?.firstOrNull()?.photo?.sizes?.getMaxImage()
                            if (url != null) {
                                val text = post.text ?: ""
                                val likes = post.likes?.count ?: 0
                                if (text.length <= 100) {
                                    memeRepository.save(
                                        MemeModel(
                                            url = url,
                                            mode = mode.key.name,
                                            text = text,
                                            sourceId = sourceId,
                                            sourceUrl = sourceUrl,
                                            likes = likes
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                delay(60000 * 30)
            }
        }
    }
}