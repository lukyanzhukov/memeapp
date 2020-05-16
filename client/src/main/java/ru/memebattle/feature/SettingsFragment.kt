package ru.memebattle.feature

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import client.common.feature.localization.LocalizationViewModel
import client.common.feature.settings.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.R
import ru.memebattle.common.feature.localization.Localization
import ru.memebattle.core.utils.openUrl
import ru.memebattle.core.utils.shareApp
import ru.memebattle.feature.signout.TrySignOutDialogFragment
import ru.memebattle.feature.signout.TrySignOutViewModel

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val viewModel: SettingsViewModel by viewModel()
    private val localizationViewModel: LocalizationViewModel by viewModel()
    private val trySignOutViewModel: TrySignOutViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        localizationViewModel.locale.platform.observe(viewLifecycleOwner) { locale ->
            text_memes_team.text = locale[Localization.SETTINGS_HASH_TAG_TEXT]
            text_rate.text = locale[Localization.SETTINGS_RATE_TEXT]
            text_share.text = locale[Localization.SETTINGS_SHARE_TEXT]
            buttonSignIn.text = locale[Localization.SETTINGS_SIGN_IN_BUTTON]
            buttonSignUp.text = locale[Localization.SETTINGS_SIGN_UP_BUTTON]

            if (viewModel.isSignedIn()) {
                buttonSignUp.text = locale[Localization.SETTINGS_LOGOUT_TEXT]
                buttonSignIn.visibility = View.GONE
            }

            cardShare.setOnClickListener {
                shareApp(
                    shareTitle = locale.getValue(Localization.SHARE_APP_TITLE),
                    shareText = locale.getValue(Localization.SHARE_APP_TEXT)
                )
            }
        }

        trySignOutViewModel.signOutEvent.observe(viewLifecycleOwner) {
            viewModel.logout()
            Navigation.findNavController(requireActivity(), R.id.host_global)
                .navigate(R.id.action_settingsFragment_to_authFragment)
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
    }

    private fun onSignInUpClick() {
        if (viewModel.isSignedIn()) {
            TrySignOutDialogFragment().show(childFragmentManager, null)
            return
        }
        Navigation.findNavController(requireActivity(), R.id.host_global)
            .navigate(R.id.action_settingsFragment_to_authFragment)
    }
}