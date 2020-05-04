package ru.memebattle.model.vk.model

data class Response(
    val count: Int?,
    val items: List<Item>?,
    val groups: List<Group?>?
)