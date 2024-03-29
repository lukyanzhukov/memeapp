package ru.memebattle.route

import com.google.gson.Gson
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.websocket.webSocket
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BroadcastChannel
import ru.memebattle.auth.BasicAuth
import ru.memebattle.auth.JwtAuth
import ru.memebattle.common.dto.AuthenticationRequestDto
import ru.memebattle.common.dto.game.GameModeModel
import ru.memebattle.common.dto.game.GameState
import ru.memebattle.common.dto.game.MemeRequest
import ru.memebattle.common.dto.game.MemeResponse
import ru.memebattle.common.dto.user.UserRegisterRequestDto
import ru.memebattle.common.model.RatingModel
import ru.memebattle.exception.LanguageNotFoundException
import ru.memebattle.localization.getLikes
import ru.memebattle.model.UserModel
import ru.memebattle.model.toDto
import ru.memebattle.repository.GameModeRepository
import ru.memebattle.repository.RateusersRepository
import ru.memebattle.service.GameFactory
import ru.memebattle.service.LocaleService
import ru.memebattle.service.ParserService
import ru.memebattle.service.UserService

class RoutingV1(
    private val userService: UserService,
    private val gameFactory: GameFactory,
    private val rateusersRepository: RateusersRepository,
    private val memeChannel: BroadcastChannel<MemeResponse>,
    private val localeService: LocaleService,
    private val gson: Gson,
    private val gameModeRepository: GameModeRepository,
    private val parserService: ParserService
) {
    fun setup(configuration: Routing) {
        with(configuration) {
            route("/api/v1/") {

                route("/") {
                    post("/registration") {
                        val input = call.receive<UserRegisterRequestDto>()
                        val response = userService.register(input.username, input.password)
                        call.respond(response)
                    }

                    post("/authentication") {
                        val input = call.receive<AuthenticationRequestDto>()
                        val response = userService.authenticate(input)
                        call.respond(response)
                    }

                    get("/rating") {
                        val modeParameter = call.request.queryParameters["gameMode"] ?: "ALL"
                        val response = rateusersRepository.getByMode(modeParameter)
                            .mapIndexed { index, rateuserModel ->
                                RatingModel(
                                    rateuserModel.name,
                                    rateuserModel.likes,
                                    index.toLong(),
                                    rateuserModel.mode
                                )
                            }
                        call.respond(response)
                    }

                    get("/memes") {
                        val response = gameFactory.getAllMemes()
                        call.respond(response)
                    }

                    get("/chill") {
                        val modeParameter = call.request.queryParameters["gameMode"]
                        val response = if (modeParameter == null) {
                            gameFactory.getAllMemes()
                        } else {
                            gameFactory.getMemesByMode(modeParameter)
                        }
                        call.respond(response)
                    }

                    route("/gameMode") {
                        get {
                            val response = gameModeRepository.getAll()
                            call.respond(response)
                        }

                        post {
                            val input = call.receive<GameModeModel>()
                            gameModeRepository.add(input)
                            call.respond("ok")
                        }

                        put {
                            val input = call.receive<GameModeModel>()
                            gameModeRepository.update(input)
                            call.respond("ok")
                        }

                        delete {
                            val id = call.request.queryParameters["id"]?.toLong() ?: return@delete
                            gameModeRepository.delete(id)
                            call.respond("ok")
                        }
                    }

                    route("parser") {
                        get {
                            parserService.startParser()
                            call.respond("ok")
                        }
                    }

                    route("restartGames") {
                        get {
                            gameFactory.restartGames()
                        }
                    }
                }

                authenticate(BasicAuth.NAME, JwtAuth.NAME, optional = true) {
                    route("/me") {
                        get {
                            call.respond(requireNotNull(me).toDto())
                        }
                    }

                    webSocket {
                        gameFactory.getStates().forEach {
                            outgoing.send(Frame.Text(gson.toJson(it)))
                        }

                        val memes = async {
                            for (memes in memeChannel.openSubscription()) {
                                if (!outgoing.isClosedForSend) {

                                    if (memes.state == GameState.RESULT) {
                                        val (language, country) = locale()

                                        val firstLikesText =
                                            getLikes(memes.likes[0], language, country)
                                        val secondLikesText =
                                            getLikes(memes.likes[1], language, country)

                                        outgoing.send(
                                            Frame.Text(
                                                gson.toJson(
                                                    memes.copy(
                                                        firstLikesText = firstLikesText,
                                                        secondLikesText = secondLikesText
                                                    )
                                                )
                                            )
                                        )
                                    } else {
                                        outgoing.send(Frame.Text(gson.toJson(memes)))
                                    }
                                }
                            }
                        }

                        val frames = async {
                            for (frame in incoming) {
                                when (frame) {
                                    is Frame.Text -> {
                                        val user = call.authentication.principal<UserModel>()
                                        val memeRequest =
                                            gson.fromJson(frame.readText(), MemeRequest::class.java)
                                        print("like $user")
                                        gameFactory.rateMeme(memeRequest, user)
                                    }
                                }
                            }
                        }

                        memes.await()
                        frames.await()
                    }
                }

                route("/locale") {
                    get {
                        val (language, country) = locale()

                        language ?: throw LanguageNotFoundException()

                        call.respond(
                            localeService.getLocale(
                                language = language,
                                country = country
                            )
                        )
                    }
                }
            }
        }
    }
}