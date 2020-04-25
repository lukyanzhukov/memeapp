package ru.memebattle.feature

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import client.common.feature.memebattle.MemeBattleState
import client.common.feature.memebattle.MemeBattleViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_memebattle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.UnstableDefault
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.R
import ru.memebattle.common.GameMode
import ru.memebattle.common.dto.game.GameState
import ru.memebattle.common.dto.game.MemeResponse
import java.util.*

class MemeBattleFragment : Fragment(R.layout.fragment_memebattle) {

    private var isButtonDisabled = true
    private var chosenMeme = -1
    private val viewModel: MemeBattleViewModel by currentScope.viewModel(this)

    @UnstableDefault
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mode = arguments?.getSerializable("GameMode") as? GameMode ?: GameMode.CLASSIC
        viewModel.setGameMode(mode)

        loadingMemesProgressBar.progress = 0
        error_button.setOnClickListener {
            viewModel.connect()
        }

        viewModel.state.platform.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MemeBattleState.Meme -> {
                    processState(state.memeResponse)
                }

                is MemeBattleState.Error -> {
                    progress.isVisible = false
                    error_group.isVisible = true
                }

                MemeBattleState.Progress -> {
                    error_group.isVisible = false
                    progress.isVisible = true
                }
            }
        }

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
    }

    private fun sendLike(num: Int) {
        isButtonDisabled = true
        chosenMeme = num
        viewModel.like(num)
    }

    private fun processState(memeResponse: MemeResponse) {
        wait_next_round_text_view.visibility = View.GONE
        memebattle_view.visibility = View.VISIBLE
        progress.isVisible = false
        error_group.isVisible = false
        when (memeResponse.state) {
            GameState.MEMES -> {
                isButtonDisabled = false
                firstWinAnimation.visibility = View.GONE
                firstWinAnimation.progress = ZERO_PROGRESS
                secondWinAnimation.visibility = View.GONE
                secondWinAnimation.progress = ZERO_PROGRESS
                like1.visibility = View.GONE
                like2.visibility = View.GONE
                result1.visibility = View.GONE
                result2.visibility = View.GONE
                val currentDate = Date()
                val endDate = Date(memeResponse.timeEnd)
                val time =
                    (endDate.time - currentDate.time - CLIENT_SERVER_DELAY) / PROGRESS_COEFFICIENT
                loadingMemesProgressBar.max = time.toInt()
                viewLifecycleOwner.lifecycleScope.launch {
                    repeat(time.toInt()) {
                        delay(50)
                        loadingMemesProgressBar.progress = (time - it).toInt()
                    }
                }
                Glide.with(requireActivity())
                    .load(memeResponse.memes[0])
                    .placeholder(resources.getDrawable(R.drawable.wait_image))
                    .into(image1)
                Glide.with(requireActivity())
                    .load(memeResponse.memes[1])
                    .placeholder(resources.getDrawable(R.drawable.wait_image))
                    .into(image2)
            }
            GameState.RESULT -> {
                loadingMemesProgressBar.progress = 0
                isButtonDisabled = true
                like1.visibility = View.GONE
                like2.visibility = View.GONE
                result1.visibility = View.VISIBLE
                result2.visibility = View.VISIBLE
                res1.text = "${memeResponse.likes[0]} likes"
                res2.text = "${memeResponse.likes[1]} likes"
                if (memeResponse.likes[0] > memeResponse.likes[1]) {
                    if (chosenMeme == 0) {
                        firstWinAnimation.visibility = View.VISIBLE
                        firstWinAnimation.playAnimation()
                    }
                } else if (memeResponse.likes[0] < memeResponse.likes[1]) {
                    if (chosenMeme == 1) {
                        secondWinAnimation.visibility = View.VISIBLE
                        secondWinAnimation.playAnimation()
                    }
                }
                chosenMeme = -1
            }
        }
    }

    companion object {
        const val PROGRESS_COEFFICIENT = 50
        const val CLIENT_SERVER_DELAY = 1000
        const val ZERO_PROGRESS = 0F
    }
}