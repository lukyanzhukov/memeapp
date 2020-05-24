package ru.memebattle.feature.game

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import client.common.feature.game.GameState
import client.common.feature.game.GameViewModel
import kotlinx.android.synthetic.main.error_loading_view.*
import kotlinx.android.synthetic.main.fragment_game.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.R
import ru.memebattle.feature.gameMode
import ru.memebattle.feature.gameonboarding.GameOnboardingDialogFragment
import ru.memebattle.feature.gameonboarding.GameOnboardingViewModel

class GameFragment : Fragment(R.layout.fragment_game) {

    private val viewModel: GameViewModel by viewModel()
    private val gameOnboardingViewModel: GameOnboardingViewModel by viewModel()

    private var selectedMode: GameModeTab = GameModeTab.CLASSIC_MODE_TAB
    private var selectedGameMode: String? = null

    private val onGameModeClick: (String) -> Unit = {
        selectedGameMode = it
        val bundle = Bundle().apply {
            gameMode = it
        }
        if (viewModel.isGameModeUsed(selectedMode.name)) {
            when (selectedMode) {
                GameModeTab.CLASSIC_MODE_TAB -> {
                    Navigation.findNavController(requireActivity(), R.id.host_global)
                        .navigate(R.id.action_mainFragment_action_to_memebattleFragment, bundle)
                }
                GameModeTab.CHILL_MODE_TAB -> {
                    Navigation.findNavController(requireActivity(), R.id.host_global)
                        .navigate(R.id.action_mainFragment_to_memeChillFragment, bundle)
                }
            }
        } else {
            GameOnboardingDialogFragment(selectedMode).show(childFragmentManager, null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectedGameMode = savedInstanceState?.gameMode
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        gameOnboardingViewModel.closeDialogEvent.observe(viewLifecycleOwner) {
            when (selectedMode) {
                GameModeTab.CLASSIC_MODE_TAB -> {
                    Navigation.findNavController(requireActivity(), R.id.host_global)
                        .navigate(
                            R.id.action_mainFragment_action_to_memebattleFragment,
                            Bundle().apply {
                                gameMode = selectedGameMode
                            })
                }
                GameModeTab.CHILL_MODE_TAB -> {
                    Navigation.findNavController(requireActivity(), R.id.host_global)
                        .navigate(
                            R.id.action_mainFragment_to_memeChillFragment,
                            Bundle().apply {
                                gameMode = selectedGameMode
                            })
                }
            }
        }

        when (selectedMode) {
            GameModeTab.CLASSIC_MODE_TAB -> {
                classic_mode_btn.background =
                    resources.getDrawable(R.drawable.bg_selected_game_mode)
                chill_mode_btn.background = null
            }
            GameModeTab.CHILL_MODE_TAB -> {
                chill_mode_btn.background = resources.getDrawable(R.drawable.bg_selected_game_mode)
                classic_mode_btn.background = null
            }
        }
        classic_mode_btn.setOnClickListener {
            classic_mode_btn.background = resources.getDrawable(R.drawable.bg_selected_game_mode)
            chill_mode_btn.background = null
            selectedMode = GameModeTab.CLASSIC_MODE_TAB
        }
        chill_mode_btn.setOnClickListener {
            chill_mode_btn.background = resources.getDrawable(R.drawable.bg_selected_game_mode)
            classic_mode_btn.background = null
            selectedMode = GameModeTab.CHILL_MODE_TAB
        }

        val ratingAdapter = GameModeAdapter(onGameModeClick)
        recyclerView.adapter = ratingAdapter

        viewModel.state.platform.observe(viewLifecycleOwner) { state ->
            when (state) {
                is GameState.Success -> {
                    waitingProgressBar.isVisible = false
                    ratingAdapter.gameModeModels = state.modes
                }

                GameState.Fail -> {
                    waitingProgressBar.isVisible = false
                    error_loading_view.isVisible = true
                }

                GameState.Progress -> {
                    waitingProgressBar.isVisible = true
                    error_loading_view.isVisible = false
                }
            }
        }

        retry_loading_button.setOnClickListener {
            viewModel.getModes()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.gameMode = selectedGameMode
    }
}