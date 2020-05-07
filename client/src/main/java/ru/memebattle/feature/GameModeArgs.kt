package ru.memebattle.feature

import android.os.Bundle
import ru.memebattle.common.GameMode

private const val KEY = "GameMode"

var Bundle.gameMode: GameMode?
    get() = getSerializable(KEY) as? GameMode
    set(value) = putSerializable(KEY, value)