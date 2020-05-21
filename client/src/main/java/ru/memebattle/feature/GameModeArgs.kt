package ru.memebattle.feature

import android.os.Bundle

private const val KEY = "GameMode"

var Bundle.gameMode: String?
    get() = getSerializable(KEY) as? String
    set(value) = putSerializable(KEY, value)