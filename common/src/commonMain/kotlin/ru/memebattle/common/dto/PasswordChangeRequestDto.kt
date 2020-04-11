package ru.memebattle.common.dto

import kotlinx.serialization.Serializable

@Serializable
class PasswordChangeRequestDto(val old: String, val new: String)
