package ru.memebattle.service

import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.request.get
import kotlinx.coroutines.*
import ru.memebattle.model.MemeModel
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
                val groupIds = listOf("183566984","120891224", "155464693", "154906069")
                groupIds.forEach { groupId ->
                    val response =
                        client.get<String>("https://api.vk.com/method/wall.get?owner_id=-$groupId&access_token=2f0935dcc011e41fdd54052842b577d80739bbca71728837395626c0a00c4fa68e6e2ecedc412cb0d1a40&v=5.103")
                    val vkResponse = Gson().fromJson(response, VKResponse::class.java)
                    val urls = vkResponse.response?.items?.map {
                        it.attachments?.firstOrNull()?.photo?.sizes?.getMaxImage()
                    }
                    urls?.forEach { url ->
                        url?.let { memeRepository.save(MemeModel(url = url)) }
                    }
                }
                delay(60000 * 5)
            }
        }
    }
}