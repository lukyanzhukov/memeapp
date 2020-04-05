package ru.memebattle.feature

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import com.bumptech.glide.Glide
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.wss
import io.ktor.client.request.header
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import kotlinx.android.synthetic.main.fragment_memebattle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.get
import ru.memebattle.PREFS_TOKEN
import ru.memebattle.R
import ru.memebattle.common.dto.game.GameState
import ru.memebattle.common.dto.game.MemeRequest
import ru.memebattle.common.dto.game.MemeResponse
import ru.memebattle.core.BaseFragment
import ru.memebattle.core.utils.getString
import ru.memebattle.core.utils.log

class MemeBattleFragment : BaseFragment() {

    private val prefs: SharedPreferences = get()
    private var isButtonDisabled = true
    private var chosenMeme = -1
    private val memeChannel = Channel<MemeRequest>()

    val client = HttpClient {
        install(WebSockets)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_memebattle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        image1.setOnClickListener {
            if (isButtonDisabled) return@setOnClickListener
            if (like1.visibility == View.GONE && like2.visibility == View.GONE) {
                like1.visibility = View.VISIBLE
            }
            sendLike(0)
        }

        image2.setOnClickListener {
            if (isButtonDisabled) return@setOnClickListener
            if (like1.visibility == View.GONE && like2.visibility == View.GONE) {
                like2.visibility = View.VISIBLE
            }
            sendLike(1)
        }
        launch {
            try {
                client.wss(
                    method = HttpMethod.Get,
                    host = "memebattle.herokuapp.com",
                    path = "/api/v1",
                    request = { header("Authorization", "Bearer ${prefs.getString(PREFS_TOKEN)}") }
                ) {
                    val frames = async {
                        for (frame in incoming) {
                            when (frame) {
                                is Frame.Text -> {
                                    val type = MemeResponse::class.javaObjectType
                                    val memeResponse = Gson().fromJson(frame.readText(), type)
                                    log(memeResponse.toString())
                                    if (memeResponse.state == GameState.MEMES) isButtonDisabled =
                                        false
                                    withContext(Dispatchers.Main) {
                                        winAnimation.visibility = View.GONE
                                        winAnimation.pauseAnimation()
                                        winAnimation.progress = 0f
                                        processState(memeResponse)
                                    }
                                }
                            }
                        }
                    }
                    val memes = async {
                        for (memes in memeChannel) {
                            val type = MemeRequest::class.javaObjectType
                            val jsonValue = Gson().toJson(memes, type)
                            outgoing.send(Frame.Text(jsonValue))
                        }
                    }
                    frames.await()
                    memes.await()
                }
            } catch (error: Throwable) {
                log(error.toString())
                error.printStackTrace()
            }
        }
    }

    private fun sendLike(num: Int) {
        isButtonDisabled = true
        chosenMeme = num
        launch {
            memeChannel.send(MemeRequest(num))
        }
    }

    private fun processState(memeResponse: MemeResponse) {
        when (memeResponse.state) {
            GameState.START -> {
                like1.visibility = View.GONE
                like2.visibility = View.GONE
                result1.visibility = View.GONE
                result2.visibility = View.GONE
            }
            GameState.MEMES -> {
                result1.visibility = View.GONE
                result2.visibility = View.GONE
                Glide.with(requireActivity())
                    .load(memeResponse.memes[0])
                    .into(image1)
                Glide.with(requireActivity())
                    .load(memeResponse.memes[1])
                    .into(image2)
            }
            GameState.RESULT -> {
                isButtonDisabled = true
                like1.visibility = View.GONE
                like2.visibility = View.GONE
                result1.visibility = View.VISIBLE
                result2.visibility = View.VISIBLE
                res1.text = "${memeResponse.likes[0]} likes"
                res2.text = "${memeResponse.likes[1]} likes"
                if (memeResponse.likes[0] > memeResponse.likes[1]) {
                    if (chosenMeme == 0) {
                        winAnimation.visibility = View.VISIBLE
                        winAnimation.playAnimation()
                    }
                } else if (memeResponse.likes[0] < memeResponse.likes[1]) {
                    if (chosenMeme == 1) {
                        winAnimation.visibility = View.VISIBLE
                        winAnimation.playAnimation()
                    }
                }
                chosenMeme = -1
            }
        }
    }
}