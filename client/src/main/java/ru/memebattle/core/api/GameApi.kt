package ru.memebattle.core.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.memebattle.common.dto.game.MemeRequest
import ru.memebattle.common.dto.game.MemeResponse

interface GameApi {

    @GET("game")
    suspend fun getState(): MemeResponse

    @POST("game")
    suspend fun sendLike(@Body memeRequest: MemeRequest): MemeResponse
}