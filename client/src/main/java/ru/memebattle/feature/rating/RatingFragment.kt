package ru.memebattle.feature.rating

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import client.common.data.LoginSource
import client.common.feature.rating.RatingState
import client.common.feature.rating.RatingViewModel
import kotlinx.android.synthetic.main.error_loading_view.*
import kotlinx.android.synthetic.main.fragment_rating.*
import org.koin.android.ext.android.get
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.R

class RatingFragment : Fragment(R.layout.fragment_rating) {

    private val loginSource: LoginSource = get()
    private val viewModel: RatingViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ratingAdapter = RatingAdapter(loginSource.login)
        recycler_view.adapter = ratingAdapter

        viewModel.state.platform.observe(viewLifecycleOwner) { state ->
            when (state) {
                is RatingState.Success -> {
                    imageView3.isVisible = true
                    waitingProgressBar.isVisible = false
                    ratingAdapter.ratingModels = state.rating
                }

                RatingState.Fail -> {
                    imageView3.isVisible = false
                    waitingProgressBar.isVisible = false
                    error_loading_view.isVisible = true
                }

                RatingState.Progress -> {
                    imageView3.isVisible = true
                    waitingProgressBar.isVisible = true
                    error_loading_view.isVisible = false
                }
            }
        }

        retry_loading_button.setOnClickListener {
            viewModel.getRating()
        }
    }

    override fun onDestroyView() {
        recycler_view.adapter = null
        super.onDestroyView()
    }
}