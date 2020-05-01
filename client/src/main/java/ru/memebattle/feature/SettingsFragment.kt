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
import ru.memebattle.core.utils.shareApp

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val viewModel: SettingsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.isSignedIn()) {
            signButtonText.text = "Выйти из аккаунта"
        } else {
            signButtonText.text = "Войти или зарегистрироваться"
        }

        signButton.setOnClickListener {
            if (viewModel.isSignedIn()) {
                viewModel.logout()
            }
            Navigation.findNavController(requireActivity(), R.id.host_global)
                .navigate(R.id.action_settingsFragment_to_authFragment)
        }
        memebattle_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://vk.com/memsbattle")
            intent.resolveActivity(checkNotNull(context).packageManager)?.let {
                checkNotNull(context).startActivity(intent)
            }
        }

        rateApp.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${requireContext().packageName}")
                )
            )
        }
        share_app_btn.setOnClickListener {
            shareApp()
        }
    }
}