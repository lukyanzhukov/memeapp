package ru.memebattle.feature

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import client.common.feature.localization.LocalizationViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.R
 import androidx.lifecycle.observe
import ru.memebattle.common.feature.localization.Localization

class MainFragment : Fragment(R.layout.fragment_main) {

     private val localizationViewModel: LocalizationViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        localizationViewModel.locale.platform.observe(viewLifecycleOwner) { locale ->
            bottomNavigationView.menu[0].title = locale[Localization.MAIN_MENU_MEMES]
            bottomNavigationView.menu[1].title = locale[Localization.MAIN_MENU_RATING]
            bottomNavigationView.menu[2].title = locale[Localization.MAIN_MENU_SETTINGS]
        }

        val navController = Navigation.findNavController(requireActivity(), R.id.host_main)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        requireActivity().window.statusBarColor = Color.WHITE
        super.onViewCreated(view, savedInstanceState)
    }
}