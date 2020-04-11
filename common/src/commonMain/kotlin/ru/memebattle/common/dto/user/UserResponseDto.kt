package ru.memebattle.common.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDto(
    val id: Long,
    val username: String
)