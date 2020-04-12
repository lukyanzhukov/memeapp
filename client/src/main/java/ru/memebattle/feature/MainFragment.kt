 package ru.memebattle.feature

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.fragment_main.*
import ru.memebattle.R

 class MainFragment : Fragment(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navController = Navigation.findNavController(requireActivity(), R.id.host_main)

        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        super.onViewCreated(view, savedInstanceState)
    }
}