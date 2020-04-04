package ru.memebattle.core.api

import retrofit2.http.Body
import retrofit2.http.POST
import ru.memebattle.common.dto.AuthenticationRequestDto
import ru.memebattle.common.dto.AuthenticationResponseDto

interface AuthApi {

    @POST("registration")
    suspend fun signUp(@Body authenticationRequestDto: AuthenticationRequestDto): AuthenticationResponseDto

    @POST("authentication")
    suspend fun signIn(@Body authenticationRequestDto: AuthenticationRequestDto): AuthenticationResponseDto
}