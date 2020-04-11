package ru.memebattle.common.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationResponseDto(val token: String)
