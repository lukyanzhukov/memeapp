package ru.memebattle.route

import io.ktor.application.ApplicationCall
import io.ktor.websocket.WebSocketServerSession
import ru.memebattle.model.LocaleModel

fun WebSocketServerSession.locale(): LocaleModel = call.locale()

fun ApplicationCall.locale(): LocaleModel =
    LocaleModel(request.headers["language"], request.headers["country"])