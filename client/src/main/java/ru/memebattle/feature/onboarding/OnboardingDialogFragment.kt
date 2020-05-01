package ru.memebattle.feature.onboarding

import android.app.Dialog
import android.graphics.Outline
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewOutlineProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import client.common.feature.localization.LocalizationViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import ru.memebattle.R
import splitties.alertdialog.appcompat.alertDialog
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.dialog_game_onboarding.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.common.feature.localization.Localization

class OnboardingDialogFragment : DialogFragment() {

    private val onboardingViewModel: OnboardingViewModel by sharedViewModel(from = {
        requireParentFragment()
    })
    private val localizationViewModel: LocalizationViewModel by viewModel()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        requireContext().alertDialog {
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_game_onboarding, null, false)
            dialogView.onboarding_banner_view.clipToOutline = true
            val provider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setRoundRect(0, 0, view.width, view.height, 16F)
                }
            }
            dialogView.onboarding_banner_view.outlineProvider = provider
            dialogView.ok_button.setOnClickListener {
                onboardingViewModel.onCloseDialog()
                dismiss()
            }

            localizationViewModel.locale.platform.observe(this@OnboardingDialogFragment) { locale ->
                dialogView.feature_title.text = locale[Localization.ONBOARDING_TITLE]
                dialogView.feature_definition_text.text = locale[Localization.ONBOARDING_TEXT]
                dialogView.ok_button.text = locale[Localization.ONBOARDING_OK_BTN_TEXT]
            }
            setView(dialogView)
        }.apply {
            val bg = ContextCompat.getDrawable(requireContext(), R.drawable.bg_alert)
            val margins = resources.getDimensionPixelOffset(R.dimen.alert_margins)
            window?.setBackgroundDrawable(InsetDrawable(bg, margins))
            this@OnboardingDialogFragment.isCancelable = false
        }
}