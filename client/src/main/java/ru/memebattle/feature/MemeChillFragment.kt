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
import client.common.feature.memechill.MemeChillState
import client.common.feature.memechill.MemeChillViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.error_loading_view.*
import kotlinx.android.synthetic.main.fragment_meme_chill.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.R
import ru.memebattle.common.dto.game.MemeModel
import ru.memebattle.common.feature.localization.Localization
import ru.memebattle.core.utils.openUrl
import ru.memebattle.core.utils.saveImage
import ru.memebattle.core.utils.shareImage

/**
 * A simple [Fragment] subclass.
 */
class MemeChillFragment : Fragment(R.layout.fragment_meme_chill) {

    private val localizationViewModel: LocalizationViewModel by viewModel()

    private var isButtonDisabled = true
    private var firstMemeSourceUrl = ""
    private var secondMemeSourceUrl = ""
    private val viewModel: MemeChillViewModel by viewModel()

    @ExperimentalStdlibApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        localizationViewModel.locale.platform.observe(viewLifecycleOwner) { locale ->
            errorTextView.text = locale[Localization.ERROR_LOADING_TEXT]
            retry_loading_button.text = locale[Localization.ERROR_LOADING_BUTTON_TEXT]

            val saveText = locale.getValue(Localization.GAME_SAVE_IMAGE_TEXT)
            setSaveClickListener(save_first_meme_btn, image1, saveText)
            setSaveClickListener(save_second_meme_btn, image2, saveText)
        }

        toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
        val mode = arguments?.gameMode ?: "CLASSIC"
        toolbar.title = mode
        viewModel.setGameMode(mode)
        viewModel.state.platform.observe(viewLifecycleOwner) { state ->
            when (state) {
                MemeChillState.Loading -> {
                    waitingProgressBar.isVisible = true
                    error_loading_view.isVisible = false
                }
                is MemeChillState.Error -> {
                    memechill_view.isVisible = false
                    waitingProgressBar.isVisible = false
                    error_loading_view.isVisible = true
                }
                is MemeChillState.SuccessMemePair -> {
                    onNextMemesPair(state.memes)
                }
            }
        }

        image1.setOnDoubleTapListener(object : GestureDetector.OnDoubleTapListener {
            override fun onDoubleTap(e: MotionEvent?): Boolean {
                if (isButtonDisabled) return true
                like1.isVisible = true
                toNextPair()
                return true
            }

            override fun onDoubleTapEvent(e: MotionEvent?): Boolean = true
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean = true
        })

        image2.setOnDoubleTapListener(object : GestureDetector.OnDoubleTapListener {
            override fun onDoubleTap(e: MotionEvent?): Boolean {
                if (isButtonDisabled) return true
                like2.isVisible = true
                toNextPair()
                return true
            }

            override fun onDoubleTapEvent(e: MotionEvent?): Boolean = true
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean = true
        })
        retry_loading_button.setOnClickListener {
            viewModel.getMemesPair()
        }
        first_source_meme_text.setOnClickListener {
            openUrl(firstMemeSourceUrl)
        }
        second_source_meme_text.setOnClickListener {
            openUrl(secondMemeSourceUrl)
        }
        share_first_meme_btn.setOnClickListener {
            lifecycleScope.launch {
                shareImage(image1.drawable.toBitmap(), first_meme_text.text.toString())
            }
        }
        share_second_meme_btn.setOnClickListener {
            lifecycleScope.launch {
                shareImage(image2.drawable.toBitmap(), second_meme_text.text.toString())
            }
        }
        viewModel.getMemesPair()
    }

    private fun onNextMemesPair(memesPair: Pair<MemeModel, MemeModel>) {
        vk_badge_first.isVisible = false
        vk_badge_second.isVisible = false
        vk_likes_first.isVisible = false
        vk_likes_second.isVisible = false
        vk_likes_first.text = memesPair.first.likes.toString()
        vk_likes_second.text = memesPair.second.likes.toString()
        first_meme_text.isVisible = memesPair.first.text.isNotEmpty()
        second_meme_text.isVisible = memesPair.second.text.isNotEmpty()
        firstMemeSourceUrl = memesPair.first.sourceUrl
        secondMemeSourceUrl = memesPair.second.sourceUrl
        first_source_meme_text.text = "@${memesPair.first.sourceId}"
        second_source_meme_text.text = "@${memesPair.second.sourceId}"
        first_meme_text.text = memesPair.first.text
        second_meme_text.text = memesPair.second.text
        isButtonDisabled = false
        memechill_view.isVisible = true
        waitingProgressBar.isVisible = false
        shadowRes1.isVisible = false
        shadowRes2.isVisible = false
        like1.isVisible = false
        like2.isVisible = false
        save_first_meme_btn.isVisible = true
        save_second_meme_btn.isVisible = true
        share_first_meme_btn.isVisible = true
        share_second_meme_btn.isVisible = true
        Glide.with(requireActivity())
            .load(memesPair.first.url)
            .placeholder(resources.getDrawable(R.drawable.wait_image))
            .into(image1)
        Glide.with(requireActivity())
            .load(memesPair.second.url)
            .placeholder(resources.getDrawable(R.drawable.wait_image))
            .into(image2)
    }

    private fun setSaveClickListener(button: View, imageView: ImageView, saveText: String) {
        button.setOnClickListener {
            lifecycleScope.launch {
                saveImage(imageView.drawable.toBitmap(), saveText)
            }
        }
    }

    @ExperimentalStdlibApi
    private fun toNextPair() {
        isButtonDisabled = true
        shadowRes1.isVisible = true
        shadowRes2.isVisible = true
        lifecycleScope.launch {
            vk_badge_first.isVisible = true
            vk_badge_second.isVisible = true
            vk_likes_first.isVisible = true
            vk_likes_second.isVisible = true
            delay(RESULT_DELAY)
            viewModel.getMemesPair()
        }
    }

    companion object {
        private const val RESULT_DELAY = 750L
    }
}
