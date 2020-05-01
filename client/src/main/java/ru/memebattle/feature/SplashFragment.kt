package ru.memebattle.feature

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import client.common.feature.splash.SplashViewModel
import androidx.lifecycle.observe
import client.common.feature.splash.SplashState
import kotlinx.android.synthetic.main.fragment_splash.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.R

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val viewModel: SplashViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        retryButton.setOnClickListener {
            viewModel.loadLocale()
        }

        viewModel.navigation.platform.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
        }

        viewModel.state.platform.observe(viewLifecycleOwner) { state ->
            when (state) {
                SplashState.Progress -> {
                    errorGroup.isVisible = false
                    progress.isVisible = true
                }

                SplashState.Error -> {
                    errorGroup.isVisible = true
                    progress.isVisible = false
                }
            }
        }
    }
}