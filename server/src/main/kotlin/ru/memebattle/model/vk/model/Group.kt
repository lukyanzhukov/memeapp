package ru.memebattle.model.vk.model

import com.google.gson.annotations.SerializedName

data class Group(
    val id: Int? = null,
    val name: String? = null,
    @SerializedName("screen_name")
    val screenName: String? = null
)