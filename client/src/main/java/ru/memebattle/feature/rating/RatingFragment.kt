package ru.memebattle.feature.rating

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import client.common.data.LoginSource
import client.common.feature.rating.RatingState
import client.common.feature.rating.RatingViewModel
import kotlinx.android.synthetic.main.fragment_rating.*
import org.koin.android.ext.android.get
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.R

class RatingFragment : Fragment(R.layout.fragment_rating) {

    private val loginSource: LoginSource = get()
    private val viewModel: RatingViewModel by currentScope.viewModel(this)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ratingAdapter = RatingAdapter(requireNotNull(loginSource.login))
        recycler_view.adapter = ratingAdapter

        viewModel.state.platform.observe(viewLifecycleOwner) { state ->
            when (state) {
                is RatingState.Success -> {
                    hideProgress()
                    ratingAdapter.ratingModels = state.rating
                }

                RatingState.Fail -> {
                    hideProgress()
                    error_group.isVisible = true
                }

                RatingState.Progress -> {
                    showProgress()
                    error_group.isVisible = false
                }
            }
        }

        error_button.setOnClickListener {
            viewModel.getRating()
        }

        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setSubtitleTextColor(Color.WHITE)
        colToolbar.setCollapsedTitleTextColor(Color.WHITE)
        colToolbar.setExpandedTitleColor(Color.WHITE)
        toolbar.title = "Рейтинг игроков"
    }

    override fun onDestroyView() {
        recycler_view.adapter = null
        super.onDestroyView()
    }

    private fun showProgress() {
        shimmer_view_container.startShimmerAnimation()
    }

    private fun hideProgress() {
        shimmer_view_container.stopShimmerAnimation()
        shimmer_view_container.isVisible = false
    }
}