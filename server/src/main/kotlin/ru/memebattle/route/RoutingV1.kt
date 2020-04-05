package ru.memebattle.route

import com.google.gson.Gson
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.features.ParameterConversionException
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.request.receive
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.websocket.webSocket
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import ru.memebattle.auth.BasicAuth
import ru.memebattle.auth.JwtAuth
import ru.memebattle.common.dto.AuthenticationRequestDto
import ru.memebattle.common.dto.PostRequestDto
import ru.memebattle.common.dto.game.MemeRequest
import ru.memebattle.common.dto.game.MemeResponse
import ru.memebattle.common.dto.user.UserRegisterRequestDto
import ru.memebattle.common.model.RatingModel
import ru.memebattle.model.UserModel
import ru.memebattle.model.toDto
import ru.memebattle.repository.RateusersRepository
import ru.memebattle.service.FileService
import ru.memebattle.service.MemeService
import ru.memebattle.service.PostService
import ru.memebattle.service.UserService

class RoutingV1(
    private val staticPath: String,
    private val postService: PostService,
    private val fileService: FileService,
    private val userService: UserService,
    private val memeService: MemeService,
    private val rateusersRepository: RateusersRepository,
    private val memeChannel: BroadcastChannel<MemeResponse>,
    private val gson: Gson
) {
    fun setup(configuration: Routing) {
        with(configuration) {
            route("/api/v1/") {
                static("/static") {
                    files(staticPath)
                }

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
                        val response = rateusersRepository.getAll()
                            .mapIndexed { index, rateuserModel ->
                                RatingModel(
                                    rateuserModel.name,
                                    rateuserModel.likes,
                                    index.toLong()
                                )
                            }
                        call.respond(response)
                    }
                }

                authenticate(BasicAuth.NAME, JwtAuth.NAME) {
                    route("/me") {
                        get {
                            call.respond(requireNotNull(me).toDto())
                        }
                    }

                    route("/posts") {
                        get {
                            val response = postService.getAll()
                            call.respond(response)
                        }
                        get("/{id}") {
                            val id = call.parameters["id"]?.toLongOrNull()
                                ?: throw ParameterConversionException(
                                    "id",
                                    "Long"
                                )
                            val response = postService.getById(id)
                            call.respond(response)
                        }
                        post {
                            val input = call.receive<PostRequestDto>()
                            val response = postService.save(input)
                            call.respond(response)
                        }
                        delete("/{id}") {
                            val id = call.parameters["id"]?.toLongOrNull()
                                ?: throw ParameterConversionException(
                                    "id",
                                    "Long"
                                )
                        }
                    }

                    webSocket {
                        outgoing.send(Frame.Text(gson.toJson(memeService.getCurrentState())))

                        val memes = async {
                            for (memes in memeChannel.openSubscription()) {
                                if (!outgoing.isClosedForSend) {
                                    outgoing.send(Frame.Text(gson.toJson(memes)))
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
                                        memeService.rateMeme(memeRequest.number, user)
                                    }
                                }
                            }
                        }

                        memes.await()
                        frames.await()
                    }
                }

                route("/media") {
                    post {
                        val multipart = call.receiveMultipart()
                        val response = fileService.save(multipart)
                        call.respond(response)
                    }
                }
            }
        }
    }
}