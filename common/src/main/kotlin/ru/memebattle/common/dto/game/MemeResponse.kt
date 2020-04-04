package ru.memebattle.common.dto.game

data class MemeResponse(val state: GameState, val memes: List<String>, val likes: List<Int>)