package ru.memebattle.feature.signout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import client.common.feature.localization.LocalizationViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_try_sign_out.view.*
import kotlinx.android.synthetic.main.dialog_try_sign_out.view.errorTextView
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.R
import ru.memebattle.common.feature.localization.Localization

class TrySignOutDialogFragment : BottomSheetDialogFragment() {

    private val trySignOutViewModel: TrySignOutViewModel by sharedViewModel(
        from = ::requireParentFragment
    )
    private val localizationViewModel: LocalizationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        LayoutInflater.from(requireContext()).inflate(R.layout.dialog_try_sign_out, null, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isCancelable = false
        val bottomSheet = view.findViewById<View>(R.id.design_bottom_sheet)
        bottomSheet?.let { sheet ->
            val behavior = BottomSheetBehavior.from((view.parent as View))
            behavior.peekHeight = sheet.height
            sheet.parent.parent.requestLayout()
        }
        view.close_onboarding_dialog_btn.setOnClickListener {
            dismiss()
        }
        view.sign_out_btn.setOnClickListener {
            trySignOutViewModel.onSignOut()
            dismiss()
        }

        localizationViewModel.locale.platform.observe(viewLifecycleOwner) { locale ->
            view.errorTextView.text = locale[Localization.SIGN_OUT_DIALOG_TEXT]
            view.sign_out_btn.text = locale[Localization.SIGN_OUT_DIALOG_BUTTON]
        }
    }
}