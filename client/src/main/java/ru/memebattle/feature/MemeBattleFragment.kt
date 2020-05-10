package ru.memebattle.feature

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import client.common.feature.localization.LocalizationViewModel
import androidx.navigation.Navigation
import client.common.feature.memebattle.MemeBattleState
import client.common.feature.memebattle.MemeBattleViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.error_loading_view.*
import kotlinx.android.synthetic.main.fragment_memebattle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.R
import ru.memebattle.common.GameMode
import ru.memebattle.common.dto.game.GameState
import ru.memebattle.common.dto.game.MemeResponse
import ru.memebattle.common.feature.localization.Localization
import ru.memebattle.core.utils.*
import ru.memebattle.feature.firstwin.FirstWinDialogFragment
import ru.memebattle.feature.firstwin.FirstWinViewModel
import java.util.*

class MemeBattleFragment : Fragment(R.layout.fragment_memebattle) {

    private var isButtonDisabled = true
    private var chosenMeme = -1
    private val viewModel: MemeBattleViewModel by viewModel()
    private var firstMemeSourceUrl = ""
    private var secondMemeSourceUrl = ""
    private val localizationViewModel: LocalizationViewModel by viewModel()
    private val firstWinViewModel: FirstWinViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        localizationViewModel.locale.platform.observe(viewLifecycleOwner) { locale ->
            errorTextView.text = locale[Localization.ERROR_LOADING_TEXT]
            retry_loading_button.text = locale[Localization.ERROR_LOADING_BUTTON_TEXT]

            val sharingText = locale.getValue(Localization.GAME_SHARE_IMAGE_TEXT)
            setShareClickListener(share_first_meme_btn, image1, sharingText)
            setShareClickListener(share_second_meme_btn, image2, sharingText)
            val saveText = locale.getValue(Localization.GAME_SAVE_IMAGE_TEXT)
            setSaveClickListener(save_first_meme_btn, image1, saveText)
            setSaveClickListener(save_second_meme_btn, image2, saveText)
        }
        toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
        val mode = arguments?.gameMode ?: GameMode.CLASSIC
        toolbar.title = mode.name
        viewModel.setGameMode(mode)

        loadingMemesProgressBar.progress = 0

        viewModel.state.platform.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MemeBattleState.Meme -> {
                    processState(state.memeResponse)
                }

                is MemeBattleState.Error -> {
                    memebattle_view.isVisible = false
                    waitingProgressBar.isVisible = false
                    error_loading_view.isVisible = true
                }

                MemeBattleState.Progress -> {
                    waitingProgressBar.isVisible = true
                    error_loading_view.isVisible = false
                }
            }
        }

        image1.setOnDoubleTapListener(object : GestureDetector.OnDoubleTapListener {
            override fun onDoubleTap(e: MotionEvent?): Boolean {
                if (isButtonDisabled) return true
                if (like1.visibility == View.GONE && like2.visibility == View.GONE) {
                    like1.isVisible = true
                }
                sendLike(0)
                return true
            }

            override fun onDoubleTapEvent(e: MotionEvent?): Boolean = true
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean = true
        })

        image2.setOnDoubleTapListener(object : GestureDetector.OnDoubleTapListener {
            override fun onDoubleTap(e: MotionEvent?): Boolean {
                if (isButtonDisabled) return true
                if (like1.visibility == View.GONE && like2.visibility == View.GONE) {
                    like2.isVisible = true
                }
                sendLike(1)
                return true
            }

            override fun onDoubleTapEvent(e: MotionEvent?): Boolean = true
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean = true
        })

        retry_loading_button.setOnClickListener {
            viewModel.connect()
        }

        firstWinViewModel.authEvent.observe(viewLifecycleOwner) {
            Navigation.findNavController(requireActivity(), R.id.host_global)
                .navigate(R.id.action_memebattleFragment_to_authFragment)
        }

        first_source_meme_text.setOnClickListener {
            openUrl(firstMemeSourceUrl)
        }
        second_source_meme_text.setOnClickListener {
            openUrl(secondMemeSourceUrl)
        }
     }

    private fun setShareClickListener(button: View, imageView: ImageView, sharingText: String) {
        button.setOnClickListener {
            lifecycleScope.launch {
                shareImage(imageView.drawable.toBitmap(), sharingText)
            }
        }
    }

    private fun setSaveClickListener(button: View, imageView: ImageView, saveText: String) {
        button.setOnClickListener {
            lifecycleScope.launch {
                saveImage(imageView.drawable.toBitmap(), saveText)
            }
        }
    }

    private fun sendLike(num: Int) {
        isButtonDisabled = true
        chosenMeme = num
        viewModel.like(num)
    }

    private fun processState(memeResponse: MemeResponse) {
        memebattle_view.isVisible = true
        waitingProgressBar.isVisible = false
        when (memeResponse.state) {
            GameState.MEMES -> {
                first_meme_text.isVisible = memeResponse.memes[0].text.isNotEmpty()
                second_meme_text.isVisible = memeResponse.memes[1].text.isNotEmpty()
                firstMemeSourceUrl = memeResponse.memes[0].sourceUrl
                secondMemeSourceUrl = memeResponse.memes[1].sourceUrl
                first_meme_text.text = memeResponse.memes[0].text
                second_meme_text.text = memeResponse.memes[1].text
                first_source_meme_text.text = "@${memeResponse.memes[0].sourceId}"
                second_source_meme_text.text = "@${memeResponse.memes[1].sourceId}"
                isButtonDisabled = false
                firstWinAnimation.isVisible = false
                firstWinAnimation.progress = ZERO_PROGRESS
                secondWinAnimation.isVisible = false
                secondWinAnimation.progress = ZERO_PROGRESS
                like1.isVisible = false
                like2.isVisible = false
                shadowRes1.isVisible = false
                shadowRes2.isVisible = false
                res1.isVisible = false
                res2.isVisible = false
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
                    .load(memeResponse.memes[0].url)
                    .placeholder(resources.getDrawable(R.drawable.wait_image))
                    .into(image1)
                Glide.with(requireActivity())
                    .load(memeResponse.memes[1].url)
                    .placeholder(resources.getDrawable(R.drawable.wait_image))
                    .into(image2)
            }
            GameState.RESULT -> {
                loadingMemesProgressBar.progress = 0
                isButtonDisabled = true
                like1.isVisible = false
                like2.isVisible = false
                shadowRes1.isVisible = true
                shadowRes2.isVisible = true
                res1.isVisible = true
                res2.isVisible = true
                res1.text = memeResponse.firstLikesText
                res2.text = memeResponse.secondLikesText
                if (memeResponse.likes[0] > memeResponse.likes[1]) {
                    if (chosenMeme == 0) {
                        firstWinAnimation.isVisible = true
                        firstWinAnimation.playAnimation()
                        showFirstWinDialog()
                    }
                } else if (memeResponse.likes[0] < memeResponse.likes[1]) {
                    if (chosenMeme == 1) {
                        secondWinAnimation.isVisible = true
                        secondWinAnimation.playAnimation()
                        showFirstWinDialog()
                    }
                }
                chosenMeme = -1
            }
        }
    }

    private fun showFirstWinDialog() {
        if (viewModel.isFistWin()) {
            FirstWinDialogFragment().show(childFragmentManager, null)
        }
    }

    companion object {
        const val PROGRESS_COEFFICIENT = 50
        const val CLIENT_SERVER_DELAY = 1000
        const val ZERO_PROGRESS = 0F
    }
}