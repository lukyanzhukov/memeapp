package ru.memebattle.feature.firstwin

import android.graphics.Outline
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.lifecycle.observe
import client.common.feature.localization.LocalizationViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_first_win.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.R
import ru.memebattle.common.feature.localization.Localization


class FirstWinDialogFragment : BottomSheetDialogFragment() {

    private val onboardingViewModel: FirstWinViewModel by sharedViewModel(from = {
        requireParentFragment()
    })
    private val localizationViewModel: LocalizationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_first_win, null, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isCancelable = false
        view.onboarding_banner_view.clipToOutline = true
        val provider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, 12F)
            }
        }
        view.onboarding_banner_view.outlineProvider = provider
        view.skip_button.setOnClickListener {
            dismiss()
        }

        view.auth_button.setOnClickListener {
            onboardingViewModel.onAuthClicked()
            dismiss()
        }

        localizationViewModel.locale.platform.observe(viewLifecycleOwner) { locale ->
            view.feature_title.text = locale[Localization.FIRST_WIN_TITLE]
            view.first_text.text = locale[Localization.FIRST_WIN_FIRST_TEXT]
            view.second_text.text = locale[Localization.FIRST_WIN_SECOND_TEXT]
            view.auth_button.text = locale[Localization.FIRST_WIN_AUTH_TEXT]
            view.skip_button.text = locale[Localization.FIRST_WIN_SKIP_TEXT]
        }
    }
}