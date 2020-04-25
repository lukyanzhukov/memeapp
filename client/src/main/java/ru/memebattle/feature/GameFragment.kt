package ru.memebattle.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import client.common.feature.game.GameViewModel
import kotlinx.android.synthetic.main.fragment_game.*
import org.koin.android.scope.currentScope
import ru.memebattle.R
import ru.memebattle.common.GameMode
import ru.memebattle.core.utils.GameOnboardingDialogListener
import ru.memebattle.core.utils.openGameOnboardingDialog

class GameFragment : Fragment() {

    private val viewModel: GameViewModel by currentScope.inject()

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
            Navigation.findNavController(requireActivity(), R.id.host_global)
                .navigate(R.id.action_mainFragment_action_to_memebattleFragment, bundle)
            return@OnClickListener
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
        classic_game_mode_btn.setOnClickListener(gameModesClickListener)
        senior_game_mode_btn.setOnClickListener(gameModesClickListener)
        english_game_mode_btn.setOnClickListener(gameModesClickListener)
        it_game_mode_btn.setOnClickListener(gameModesClickListener)
        science_game_mode_btn.setOnClickListener(gameModesClickListener)
        work_game_mode_btn.setOnClickListener(gameModesClickListener)
        study_game_mode_btn.setOnClickListener(gameModesClickListener)
    }
}