package ru.memebattle.core.api

import retrofit2.http.GET
import ru.memebattle.common.model.RatingModel

interface RatingApi {

    @GET("rating")
    suspend fun getRating(): List<RatingModel>
}