package ru.memebattle.core.utils

import android.content.Intent
import android.graphics.Outline
import android.graphics.drawable.InsetDrawable
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.DimenRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.dialog_first_win.view.*
import kotlinx.android.synthetic.main.dialog_game_onboarding.view.*
import kotlinx.android.synthetic.main.dialog_permission_request_block.view.ok_button
import kotlinx.android.synthetic.main.dialog_game_onboarding.view.onboarding_banner_view
import ru.memebattle.R


data class GameOnboardingDialogListener(val onCloseDialog: () -> Unit)

data class FirstWinDialogListener(val onCloseDialog: () -> Unit, val onAuthClick: () -> Unit)

fun Fragment.createDialog(
    dialogView: View,
    @DimenRes marginsRes: Int = R.dimen.alert_margins
): AlertDialog {
    val dialog = AlertDialog.Builder(context!!)
        .setView(dialogView)
        .create()
    val bg = ContextCompat.getDrawable(requireContext(), R.drawable.bg_alert)
    val margins = resources.getDimensionPixelOffset(marginsRes)
    dialog.window?.setBackgroundDrawable(InsetDrawable(bg, margins))
    return dialog
}

fun Fragment.openPermissionRequestBlockDialog() {
    log("openPermissionRequestBlockDialog")
    val layoutId = R.layout.dialog_permission_request_block
    val dialogView = layoutInflater.inflate(layoutId, null, false)
    val dialog = createDialog(dialogView)
    dialog.setCancelable(false)
    dialogView.ok_button.setOnClickListener {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireActivity().packageName, null)
        )
        requireActivity().startActivity(intent)
        dialog.dismiss()
    }
    dialog.show()
}

fun Fragment.openGameOnboardingDialog(listener: GameOnboardingDialogListener) {
    val layoutId = R.layout.dialog_game_onboarding
    val dialogView = layoutInflater.inflate(layoutId, null, false)
    val dialog = createDialog(dialogView)

    val provider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, 16F)
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
    val dialog = createDialog(dialogView)

    val provider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, 16F)
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