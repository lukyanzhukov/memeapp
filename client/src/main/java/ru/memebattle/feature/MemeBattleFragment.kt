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
import kotlinx.coroutines.*
import kotlinx.serialization.UnstableDefault
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.R
import ru.memebattle.common.dto.game.GameState
import ru.memebattle.common.dto.game.MemeResponse

class MemeBattleFragment : Fragment(R.layout.fragment_memebattle) {

    private var isButtonDisabled = true
    private var chosenMeme = -1
    private val viewModel: MemeBattleViewModel by currentScope.viewModel(this)

    @UnstableDefault
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        progress.isVisible = false
        error_group.isVisible = false
        when (memeResponse.state) {
            GameState.START -> {
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                    repeat(DELAY_RESULTS_SECONDS_TIME) {
                        delay(1000)
                        loadingMemesProgressBar.incrementProgressBy(FULL_PROGRESS / DELAY_RESULTS_SECONDS_TIME)
                    }
                }
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

    companion object {
        const val FULL_PROGRESS = 100
        const val DELAY_RESULTS_SECONDS_TIME = 10
    }
}