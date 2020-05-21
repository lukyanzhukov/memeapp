package ru.memebattle.feature.game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import client.common.feature.game.GameViewModel
import kotlinx.android.synthetic.main.fragment_game.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.R
import ru.memebattle.feature.gameMode
import ru.memebattle.feature.onboarding.OnboardingDialogFragment
import ru.memebattle.feature.onboarding.OnboardingViewModel

class GameFragment : Fragment(R.layout.fragment_game) {

    private val viewModel: GameViewModel by viewModel()
    private val onboardingViewModel: OnboardingViewModel by viewModel()

    private var selectedMode: Int = CLASSIC_MODE_TAB_INDEX
    private var selectedGameMode: String? = null

    private val gameModesClickListener = View.OnClickListener {
        when (it.id) {
            R.id.classic_game_mode_btn -> selectedGameMode = "CLASSIC"
            R.id.senior_game_mode_btn -> selectedGameMode = "SENIOR"
            R.id.english_game_mode_btn -> selectedGameMode = "ENGLISH"
            R.id.it_game_mode_btn -> selectedGameMode = "IT"
            R.id.science_game_mode_btn -> selectedGameMode = "SCIENCE"
            R.id.work_game_mode_btn -> selectedGameMode = "WORK"
            R.id.study_game_mode_btn -> selectedGameMode = "STUDY"
        }
        val bundle = Bundle().apply {
            gameMode = selectedGameMode
        }
        // Сейчас проверяется и ставится только классический режим, в след. релизе будет чекаться каждый.
        if (viewModel.isGameModeUsed("CLASSIC")) {
            when (selectedMode) {
                CLASSIC_MODE_TAB_INDEX -> {
                    Navigation.findNavController(requireActivity(), R.id.host_global)
                        .navigate(R.id.action_mainFragment_action_to_memebattleFragment, bundle)
                    return@OnClickListener
                }
                CHILL_MODE_TAB_INDEX -> {
                    Navigation.findNavController(requireActivity(), R.id.host_global)
                        .navigate(R.id.action_mainFragment_to_memeChillFragment, bundle)
                    return@OnClickListener
                }
                else -> return@OnClickListener
            }
        }

        OnboardingDialogFragment().show(childFragmentManager, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectedGameMode = savedInstanceState?.gameMode
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onboardingViewModel.closeDialogEvent.observe(viewLifecycleOwner) {
            Navigation.findNavController(requireActivity(), R.id.host_global)
                .navigate(R.id.action_mainFragment_action_to_memebattleFragment, Bundle().apply {
                    gameMode = selectedGameMode
                })
        }

        when (selectedMode) {
            CLASSIC_MODE_TAB_INDEX -> {
                classic_mode_btn.background =
                    resources.getDrawable(R.drawable.bg_selected_game_mode)
                chill_mode_btn.background = null
            }
            CHILL_MODE_TAB_INDEX -> {
                chill_mode_btn.background = resources.getDrawable(R.drawable.bg_selected_game_mode)
                classic_mode_btn.background = null
            }
        }
        classic_mode_btn.setOnClickListener {
            classic_mode_btn.background = resources.getDrawable(R.drawable.bg_selected_game_mode)
            chill_mode_btn.background = null
            selectedMode = CLASSIC_MODE_TAB_INDEX
        }
        chill_mode_btn.setOnClickListener {
            chill_mode_btn.background = resources.getDrawable(R.drawable.bg_selected_game_mode)
            classic_mode_btn.background = null
            selectedMode = CHILL_MODE_TAB_INDEX
        }
        classic_game_mode_btn.setOnClickListener(gameModesClickListener)
        senior_game_mode_btn.setOnClickListener(gameModesClickListener)
        english_game_mode_btn.setOnClickListener(gameModesClickListener)
        it_game_mode_btn.setOnClickListener(gameModesClickListener)
        science_game_mode_btn.setOnClickListener(gameModesClickListener)
        work_game_mode_btn.setOnClickListener(gameModesClickListener)
        study_game_mode_btn.setOnClickListener(gameModesClickListener)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.gameMode = selectedGameMode
    }

    companion object {
        private const val CLASSIC_MODE_TAB_INDEX = 0
        private const val CHILL_MODE_TAB_INDEX = 1
    }
}