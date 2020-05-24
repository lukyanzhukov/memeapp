package ru.memebattle.feature.rating

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import client.common.data.LoginSource
import client.common.feature.localization.LocalizationViewModel
import client.common.feature.rating.RatingState
import client.common.feature.rating.RatingViewModel
import kotlinx.android.synthetic.main.error_loading_view.*
import kotlinx.android.synthetic.main.fragment_rating.*
import org.koin.android.ext.android.get
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.R
import ru.memebattle.common.feature.localization.Localization

class RatingFragment : Fragment(R.layout.fragment_rating) {

    private val loginSource: LoginSource = get()
    private val viewModel: RatingViewModel by viewModel()
    private val localizationViewModel: LocalizationViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ratingAdapter = RatingAdapter(loginSource.login)
        val modesAdapter = GameModesAdapter()
        modesAdapter.onItemSelected = {
            viewModel.getRating(it)
        }
        recycler_view.adapter = ratingAdapter
        modes_recycler.adapter = modesAdapter
        viewModel.getModes()
        localizationViewModel.locale.platform.observe(viewLifecycleOwner) { locale ->
            errorTextView.text = locale[Localization.ERROR_LOADING_TEXT]
            retry_loading_button.text = locale[Localization.ERROR_LOADING_BUTTON_TEXT]
        }

        viewModel.state.platform.observe(viewLifecycleOwner) { state ->
            when (state) {
                is RatingState.Success -> {
                    modes_recycler.isVisible = true
                    recycler_view.isVisible = true
                    imageView3.isVisible = true
                    waitingProgressBar.isVisible = false
                    if (state.modes != null) {
                        val modes = state.modes?.map {
                            RatingGameModeItem(it.name, R.drawable.bg_item_study_mode)
                        }?.toMutableList()
                        modes?.add(0, RatingGameModeItem("All", R.drawable.bg_item_study_mode))
                        if (modes != null) {
                            modesAdapter.ratingGameModesModels = modes
                        }
                    }
                    ratingAdapter.ratingModels = state.rating
                }

                RatingState.Fail -> {
                    modes_recycler.isVisible = false
                    recycler_view.isVisible = false
                    imageView3.isVisible = false
                    waitingProgressBar.isVisible = false
                    error_loading_view.isVisible = true
                }

                RatingState.Progress -> {
                    modes_recycler.isVisible = true
                    recycler_view.isVisible = false
                    imageView3.isVisible = true
                    waitingProgressBar.isVisible = true
                    error_loading_view.isVisible = false
                }
            }
        }

        retry_loading_button.setOnClickListener {
            viewModel.getRating("ALL")
        }
    }

    override fun onDestroyView() {
        recycler_view.adapter = null
        super.onDestroyView()
    }
}