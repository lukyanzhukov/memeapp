package ru.memebattle.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_memebattle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import ru.memebattle.R
import ru.memebattle.common.dto.game.MemeRequest
import ru.memebattle.common.dto.game.MemeResponse
import ru.memebattle.core.BaseFragment
import ru.memebattle.core.api.GameApi
import ru.memebattle.core.utils.log
import ru.memebattle.core.utils.toast

class MemeBattleFragment : BaseFragment() {

    private val gameApi: GameApi = get()

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
            if (like1.visibility == View.GONE && like2.visibility == View.GONE) {
                like1.visibility = View.VISIBLE
            }
            sendLike(0)
        }

        image2.setOnClickListener {
            if (like1.visibility == View.GONE && like2.visibility == View.GONE) {
                like2.visibility = View.VISIBLE
            }
            sendLike(1)
        }

        launch {
            while (true) {
                if (!isActive) {
                    return@launch
                }

                try {
                    val state = gameApi.getState()

                    log("get state res $state")
                    processState(state)


                    delay(1000)
                } catch (e: Exception) {
                        log("get state err $e")
                        toast(e.localizedMessage.orEmpty())
                    }
            }
        }
    }

    private fun sendLike(num: Int) {
        launch {

            try {
                val state = gameApi.sendLike(MemeRequest(num))

                log("send like res $state")
                processState(state)
            } catch (e: Exception) {
                log("send like err $e")
                toast(e.localizedMessage.orEmpty())
            }
        }
    }

    private fun processState(memeResponse: MemeResponse) {
        when (memeResponse.state) {
            "start" -> {
                like1.visibility = View.GONE
                like2.visibility = View.GONE
                result1.visibility = View.GONE
                result2.visibility = View.GONE
            }
            "memes" -> {
                result1.visibility = View.GONE
                result2.visibility = View.GONE
                Glide.with(requireActivity())
                    .load(memeResponse.memes[0])
                    .into(image1)
                Glide.with(requireActivity())
                    .load(memeResponse.memes[1])
                    .into(image2)
            }
            "result" -> {
                like1.visibility = View.GONE
                like2.visibility = View.GONE
                result1.visibility = View.VISIBLE
                result2.visibility = View.VISIBLE
                res1.text = "${memeResponse.likes[0]} likes"
                res2.text = "${memeResponse.likes[1]} likes"
            }
        }
    }
}