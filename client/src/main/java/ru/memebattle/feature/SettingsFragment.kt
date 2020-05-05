package ru.memebattle.feature

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import client.common.feature.settings.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.R
import ru.memebattle.core.utils.openUrl
import ru.memebattle.core.utils.shareApp

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val viewModel: SettingsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.isSignedIn()) {
            buttonSignUp.text = "Выйти из аккаунта"
            buttonSignIn.visibility = View.GONE
        }

        buttonSignIn.setOnClickListener {
            onSignInUpClick()
        }

        buttonSignUp.setOnClickListener {
            onSignInUpClick()
        }

        cardMemesTeam.setOnClickListener {
            openUrl("https://vk.com/memsbattle")
        }

        cardRate.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${requireContext().packageName}")
                )
            )
        }
        cardShare.setOnClickListener {
            shareApp()
        }
    }

    private fun onSignInUpClick() {
        if (viewModel.isSignedIn()) {
            viewModel.logout()
        }
        Navigation.findNavController(requireActivity(), R.id.host_global)
            .navigate(R.id.action_settingsFragment_to_authFragment)
    }
}