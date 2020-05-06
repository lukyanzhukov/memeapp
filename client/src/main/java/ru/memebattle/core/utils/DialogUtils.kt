package ru.memebattle.core.utils

import android.app.Activity
import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_fatal_error.view.*
import kotlinx.android.synthetic.main.dialog_first_win.view.*
import kotlinx.android.synthetic.main.dialog_game_onboarding.view.*
import kotlinx.android.synthetic.main.dialog_game_onboarding.view.onboarding_banner_view
import kotlinx.android.synthetic.main.dialog_permission_request_block.view.ok_button
import ru.memebattle.R


data class FatalErrorDialogListener(val onCloseDialog: () -> Unit)

data class GameOnboardingDialogListener(val onCloseDialog: () -> Unit)

data class FirstWinDialogListener(val onCloseDialog: () -> Unit, val onAuthClick: () -> Unit)

fun Activity.openFatalErrorDialog(listener: FatalErrorDialogListener) {
    val layoutId = R.layout.dialog_fatal_error
    val dialogView = layoutInflater.inflate(layoutId, null, false)
    val dialog = BottomSheetDialog(this)
    dialog.setContentView(dialogView)
    dialog.setOnShowListener {
        val dialog = it as BottomSheetDialog
        val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
        bottomSheet?.let { sheet ->
            dialog.behavior.peekHeight = sheet.height
            sheet.parent.parent.requestLayout()
        }
        dialogView.back_button.setOnClickListener {
            dialog.dismiss()
            listener.onCloseDialog()
        }
    }
    dialog.setCancelable(false)
    dialog.show()
}

fun Fragment.openGameOnboardingDialog(listener: GameOnboardingDialogListener) {
    val layoutId = R.layout.dialog_game_onboarding
    val dialogView = layoutInflater.inflate(layoutId, null, false)
    val dialog = BottomSheetDialog(requireContext())
    dialog.setContentView(dialogView)
    dialog.setOnShowListener {
        val dialog = it as BottomSheetDialog
        val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
        bottomSheet?.let { sheet ->
            dialog.behavior.peekHeight = sheet.height
            sheet.parent.parent.requestLayout()
        }
    }
    dialogView.close_onboarding_dialog_btn.setOnClickListener {
        dialog.dismiss()
    }
    val provider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, 12F)
        }
    }

    dialog.setCancelable(false)
    dialogView.onboarding_banner_view.clipToOutline = true
    dialogView.onboarding_banner_view.outlineProvider = provider
    dialogView.ok_button.setOnClickListener {
        listener.onCloseDialog()
        dialog.dismiss()
    }
    dialog.show()
}

fun Fragment.openFirstWinDialog(listener: FirstWinDialogListener) {

    val layoutId = R.layout.dialog_first_win
    val dialogView = layoutInflater.inflate(layoutId, null, false)
    val dialog = BottomSheetDialog(requireContext())
    dialog.setContentView(dialogView)
    val provider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, 12F)
        }
    }

    dialog.setCancelable(false)
    dialogView.onboarding_banner_view.clipToOutline = true
    dialogView.onboarding_banner_view.outlineProvider = provider
    dialogView.skip_button.setOnClickListener {
        listener.onCloseDialog()
        dialog.dismiss()
    }
    dialogView.auth_button.setOnClickListener {
        listener.onAuthClick()
        dialog.dismiss()
    }
    dialog.show()
}