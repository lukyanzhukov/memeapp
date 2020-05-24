package ru.memebattle.feature.gameonboarding

import android.app.Dialog
import android.content.res.Resources
import android.graphics.Outline
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewOutlineProvider
import androidx.lifecycle.observe
import client.common.feature.localization.LocalizationViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_game_onboarding.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.R
import ru.memebattle.common.feature.localization.Localization
import ru.memebattle.feature.game.GameModeTab

class GameOnboardingDialogFragment(private val selectedMode: GameModeTab) :
    BottomSheetDialogFragment() {

    private val gameOnboardingViewModel: GameOnboardingViewModel by sharedViewModel(from = {
        requireParentFragment()
    })
    private val localizationViewModel: LocalizationViewModel by viewModel()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_game_onboarding, null, false)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheetInternal = d.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheetInternal?.let {
                BottomSheetBehavior.from(it).peekHeight =
                    Resources.getSystem().displayMetrics.heightPixels
            }
        }

        val provider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, 12F)
            }
        }
        dialogView.onboarding_banner_view.clipToOutline = true
        dialogView.onboarding_banner_view.outlineProvider = provider
        dialogView.ok_button.setOnClickListener {
            gameOnboardingViewModel.onCloseDialog()
            dismiss()
        }
        dialogView.close_onboarding_dialog_btn.setOnClickListener {
            dismiss()
        }

        localizationViewModel.locale.platform.observe(this@GameOnboardingDialogFragment) { locale ->
            when (selectedMode) {
                GameModeTab.CLASSIC_MODE_TAB -> {
                    dialogView.onboarding_banner_view.setImageResource(R.drawable.classic_game_onboarding_banner)
                    dialogView.feature_title.text =
                        locale[Localization.CLASSIC_GAME_ONBOARDING_TITLE]
                    dialogView.feature_definition_text.text =
                        locale[Localization.CLASSIC_GAME_ONBOARDING_TEXT]
                }
                GameModeTab.CHILL_MODE_TAB -> {
                    dialogView.onboarding_banner_view.setImageResource(R.drawable.chill_game_onboarding_banner)
                    dialogView.feature_title.text =
                        locale[Localization.CHILL_GAME_ONBOARDING_TITLE]
                    dialogView.feature_definition_text.text =
                        locale[Localization.CHILL_GAME_ONBOARDING_TEXT]
                }
            }
            dialogView.ok_button.text = locale[Localization.ONBOARDING_OK_BTN_TEXT]
        }
        dialog.setContentView(dialogView)
        this@GameOnboardingDialogFragment.isCancelable = true
        return dialog
    }
}