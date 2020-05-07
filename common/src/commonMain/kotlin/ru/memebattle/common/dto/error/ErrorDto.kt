package ru.memebattle.common.dto.error

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDto(val message: String)