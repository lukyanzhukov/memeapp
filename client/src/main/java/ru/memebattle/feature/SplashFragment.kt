package ru.memebattle.feature

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import client.common.feature.splash.SplashViewModel
import androidx.lifecycle.observe
import client.common.feature.splash.NavState
import client.common.feature.splash.SplashState
import kotlinx.android.synthetic.main.error_loading_view.*
import kotlinx.android.synthetic.main.fragment_splash.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.R
import ru.memebattle.core.utils.log

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val viewModel: SplashViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.navigation.platform.observe(viewLifecycleOwner) {
            when (it) {
                NavState.Main -> findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
                NavState.Onboarding -> findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
            }
        }

        viewModel.state.platform.observe(viewLifecycleOwner) { state ->
            when (state) {
                SplashState.Progress -> {
                    error_loading_view.isVisible = false
                    textView.isVisible = true
                    imageView2.isVisible = true
                }

                SplashState.Error -> {
                    error_loading_view.isVisible = true
                    textView.isVisible = false
                    imageView2.isVisible = false
                }
            }
        }

        retry_loading_button.setOnClickListener {
            viewModel.loadLocale()
        }
    }
}