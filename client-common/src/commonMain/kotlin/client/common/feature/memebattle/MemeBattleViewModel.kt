package client.common.feature.memebattle

import client.common.data.TokenSource
import client.common.feature.localization.DeviceLocaleSource
import client.common.data.*
import client.common.presentation.LiveData
import client.common.presentation.MutableLiveData
import client.common.presentation.ViewModel
import client.common.presentation.viewModelScope
import client.uiDispatcher
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import ru.memebattle.common.GameMode
import ru.memebattle.common.dto.game.GameState
import ru.memebattle.common.dto.game.MemeRequest
import ru.memebattle.common.dto.game.MemeResponse

class MemeBattleViewModel(
    private val client: HttpClient,
    private val tokenSource: TokenSource,
    private val deviceLocaleSource: DeviceLocaleSource
) : ViewModel() {

    private lateinit var mode: GameMode
    private val memeChannel = Channel<MemeRequest>()
    private val _state = MutableLiveData<MemeBattleState>()
    val state: LiveData<MemeBattleState>
        get() = _state

    fun connect() {
        _state.value = MemeBattleState.Progress
        viewModelScope.launch {
            try {
                client.memeSocket(
                    protocol = URLProtocol.byName[socketProtocol()] ?: URLProtocol.WSS,
                    method = HttpMethod.Get,
                    host = socketHost(),
                    port = socketPort(),
                    path = "/api/v1",
                    request = {
                        if (tokenSource.token != null) {
                            header("Authorization", "Bearer ${tokenSource.token}")
                        }
                        header("language", deviceLocaleSource.getLanguage())
                        header("country", deviceLocaleSource.getCountry())
                    }
                ) {
                    val frames = async {
                        for (frame in incoming) {
                            when (frame) {
                                is Frame.Text -> {
                                    @Suppress("EXPERIMENTAL_API_USAGE")
                                    val memeResponse: MemeResponse =
                                        Json.parse(MemeResponse.serializer(), frame.readText())
                                    if (memeResponse.gameMode == mode) {
                                        if (!(memeResponse.state == GameState.RESULT && _state.value == null)) {
                                            withContext(uiDispatcher()) {
                                                _state.value = MemeBattleState.Meme(memeResponse)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Suppress("EXPERIMENTAL_API_USAGE")
                    val memes = async {
                        for (memes in memeChannel) {
                            val jsonValue = Json.stringify(MemeRequest.serializer(), memes)
                            outgoing.send(Frame.Text(jsonValue))
                        }
                    }
                    frames.await()
                    memes.await()
                }
            } catch (error: Throwable) {
                _state.value = MemeBattleState.Error(error.message)
            }
        }
    }

    fun like(num: Int) {
        viewModelScope.launch {
            memeChannel.send(MemeRequest(num, mode))
        }
    }

    fun setGameMode(mode: GameMode) {
        this.mode = mode
    }

    fun isFistWin(): Boolean {
        return if (tokenSource.token == null && !tokenSource.hasWin) {
            tokenSource.hasWin = true
            true
        } else {
            false
        }
    }
}