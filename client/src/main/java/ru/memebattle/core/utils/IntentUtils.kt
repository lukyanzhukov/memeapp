package ru.memebattle.core.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import ru.memebattle.R
import java.util.*

private const val IMAGE_TITLE_SUFFIX = "-MemeApp"
private const val SAVED_IMAGE_TYPE = "image/jpeg"
private const val SHARE_LINK_TEXT_TYPE = "text/plain"

/** Позволяет поделиться bitmap изображением с помощью системного sharing-диалога. */
fun Fragment.shareImage(bitmap: Bitmap, imageText: String) {
    val title = Date().time.toString() + IMAGE_TITLE_SUFFIX
    val bitmapPath: String =
        MediaStore.Images.Media.insertImage(requireActivity().contentResolver, bitmap, title, null)
    val bitmapUri = Uri.parse(bitmapPath)
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            requireActivity().getString(R.string.share_image_text, imageText)
        )
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        putExtra(Intent.EXTRA_STREAM, bitmapUri)
        type = SAVED_IMAGE_TYPE
    }
    requireActivity().startActivity(
        Intent.createChooser(intent, title).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    )
}

/** Позволяет сохранить изображение. */
fun Fragment.saveImage(bitmap: Bitmap) {
    val title = Date().time.toString() + IMAGE_TITLE_SUFFIX
    MediaStore.Images.Media.insertImage(requireActivity().contentResolver, bitmap, title, null)
    toast(getString(R.string.save_image_text, title))
}

/** Позволяет поделиться ссылкой на приложение с помощью системного sharing-диалога. */
fun Fragment.shareApp() {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = SHARE_LINK_TEXT_TYPE
        putExtra(Intent.EXTRA_TEXT, requireActivity().getString(R.string.share_app_text))
    }
    requireActivity().startActivity(
        Intent.createChooser(intent, getString(R.string.share_app_title)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    )
}