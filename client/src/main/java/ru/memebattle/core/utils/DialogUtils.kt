package ru.memebattle.core.utils

import android.graphics.Outline
import android.graphics.drawable.InsetDrawable
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.DimenRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.dialog_game_onboarding.view.*
import ru.memebattle.R

data class GameOnboardingDialogListener(val onCloseDialog: () -> Unit)

fun Fragment.createDialog(
    dialogView: View,
    @DimenRes marginsRes: Int = R.dimen.alert_margins
): AlertDialog {
    val dialog = AlertDialog.Builder(context!!)
        .setView(dialogView)
        .create()
    val bg = ContextCompat.getDrawable(context!!, R.drawable.bg_alert)
    val margins = resources.getDimensionPixelOffset(marginsRes)
    dialog.window?.setBackgroundDrawable(InsetDrawable(bg, margins))
    return dialog
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