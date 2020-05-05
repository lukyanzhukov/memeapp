package ru.memebattle.feature

import android.os.Bundle
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import client.common.feature.memechill.MemeChillState
import client.common.feature.memechill.MemeChillViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.error_loading_view.*
import kotlinx.android.synthetic.main.fragment_meme_chill.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.R
import ru.memebattle.common.GameMode
import ru.memebattle.common.dto.game.MemeModel
import ru.memebattle.core.utils.openUrl
import ru.memebattle.core.utils.saveImage
import ru.memebattle.core.utils.shareImage

/**
 * A simple [Fragment] subclass.
 */
class MemeChillFragment : Fragment(R.layout.fragment_meme_chill) {

    private var isButtonDisabled = true
    private var firstMemeSourceUrl = ""
    private var secondMemeSourceUrl = ""
    private val viewModel: MemeChillViewModel by viewModel()
    private val onSaveClickListener: (v: View) -> Unit = {
        when (it.id) {
            R.id.save_first_meme_btn -> {
                saveImage(image1.drawable.toBitmap())
            }
            R.id.save_second_meme_btn -> {
                saveImage(image2.drawable.toBitmap())
            }
        }
    }
    private val onShareClickListener: (v: View) -> Unit = {
        when (it.id) {
            R.id.share_first_meme_btn -> {
                shareImage(image1.drawable.toBitmap(), first_meme_text.text.toString())
            }
            R.id.share_second_meme_btn -> {
                shareImage(image2.drawable.toBitmap(), second_meme_text.text.toString())
            }
        }
    }

    @ExperimentalStdlibApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
        val mode = arguments?.getSerializable("GameMode") as? GameMode ?: GameMode.CLASSIC
        toolbar.title = mode.name
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

        image1.setOnClickListener {
            shadowRes1.isVisible = true
            shadowRes2.isVisible = true
            like1.isVisible = true
            lifecycleScope.launch {
                delay(RESULT_DELAY)
                viewModel.getMemesPair()
            }
        }

        image2.setOnClickListener {
            shadowRes1.isVisible = true
            shadowRes2.isVisible = true
            like2.isVisible = true
            lifecycleScope.launch {
                delay(RESULT_DELAY)
                viewModel.getMemesPair()
            }
        }
        retry_loading_button.setOnClickListener {
            viewModel.getMemesPair()
        }
        first_source_meme_text.setOnClickListener {
            openUrl(firstMemeSourceUrl)
        }
        second_source_meme_text.setOnClickListener {
            openUrl(secondMemeSourceUrl)
        }
        save_first_meme_btn.setOnClickListener(onSaveClickListener)
        save_second_meme_btn.setOnClickListener(onSaveClickListener)
        share_first_meme_btn.setOnClickListener(onShareClickListener)
        share_second_meme_btn.setOnClickListener(onShareClickListener)
        viewModel.getMemesPair()
    }

    private fun onNextMemesPair(memesPair: Pair<MemeModel, MemeModel>) {
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

    companion object {
        private const val RESULT_DELAY = 500L
    }
}
