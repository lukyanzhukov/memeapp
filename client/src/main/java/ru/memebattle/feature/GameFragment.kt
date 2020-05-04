package ru.memebattle.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import client.common.feature.game.GameViewModel
import kotlinx.android.synthetic.main.fragment_game.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.R
import ru.memebattle.common.GameMode
import ru.memebattle.core.utils.GameOnboardingDialogListener
import ru.memebattle.core.utils.openGameOnboardingDialog

class GameFragment : Fragment() {

    private val viewModel: GameViewModel by viewModel()
    private var selectedMode: Int = CLASSIC_MODE_TAB_INDEX

    private val gameModesClickListener = View.OnClickListener {
        val bundle = Bundle()
        when (it.id) {
            R.id.classic_game_mode_btn -> bundle.putSerializable("GameMode", GameMode.CLASSIC)
            R.id.senior_game_mode_btn -> bundle.putSerializable("GameMode", GameMode.SENIOR)
            R.id.english_game_mode_btn -> bundle.putSerializable("GameMode", GameMode.ENGLISH)
            R.id.it_game_mode_btn -> bundle.putSerializable("GameMode", GameMode.IT)
            R.id.science_game_mode_btn -> bundle.putSerializable("GameMode", GameMode.SCIENCE)
            R.id.work_game_mode_btn -> bundle.putSerializable("GameMode", GameMode.WORK)
            R.id.study_game_mode_btn -> bundle.putSerializable("GameMode", GameMode.STUDY)
        }
        // Сейчас проверяется и ставится только классический режим, в след. релизе будет чекаться каждый.
        if (viewModel.isGameModeUsed(GameMode.CLASSIC)) {
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
        openGameOnboardingDialog(GameOnboardingDialogListener {
            Navigation.findNavController(requireActivity(), R.id.host_global)
                .navigate(R.id.action_mainFragment_action_to_memebattleFragment, bundle)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    companion object {
        private const val CLASSIC_MODE_TAB_INDEX = 0
        private const val CHILL_MODE_TAB_INDEX = 1
    }
}