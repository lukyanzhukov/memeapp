package ru.memebattle.feature

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import client.common.feature.splash.SplashNavigation
import client.common.feature.splash.SplashViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.R

class SplashFragment : Fragment() {

    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when (viewModel.getRoute()) {
            SplashNavigation.ToMain -> findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
            SplashNavigation.ToAuth -> findNavController().navigate(R.id.action_splashFragment_to_authFragment)
        }
    }
}