package ru.memebattle.feature.fatal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import client.common.feature.localization.LocalizationViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_fatal_error.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.R
import ru.memebattle.common.feature.localization.Localization

class FatalDialogFragment : BottomSheetDialogFragment() {

    private val fatalViewModel: FatalViewModel by sharedViewModel(from =::requireActivity)
    private val localizationViewModel: LocalizationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        LayoutInflater.from(requireContext()).inflate(R.layout.dialog_fatal_error, null, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isCancelable = false
        val bottomSheet = view.findViewById<View>(R.id.design_bottom_sheet)
        bottomSheet?.let { sheet ->
            val behavior = BottomSheetBehavior.from((view.parent as View))
            behavior.peekHeight = sheet.height
            sheet.parent.parent.requestLayout()
        }
        view.back_button.setOnClickListener {
            dismiss()
            fatalViewModel.onCloseDialog()
        }

        localizationViewModel.locale.platform.observe(viewLifecycleOwner) { locale ->
            view.errorTextView.text = locale[Localization.FATAL_ERROR_DIALOG_TEXT]
            view.back_button.text = locale[Localization.FATAL_ERROR_DIALOG_BTN_TEXT]
        }
    }
}