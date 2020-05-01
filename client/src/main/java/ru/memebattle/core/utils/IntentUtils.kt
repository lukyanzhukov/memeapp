package ru.memebattle.core.utils

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.memebattle.R
import java.util.*

private const val IMAGE_TITLE_SUFFIX = "-MemeApp"
private const val SAVED_IMAGE_TYPE = "image/jpeg"
private const val SHARE_LINK_TEXT_TYPE = "text/plain"

/** Позволяет поделиться bitmap изображением с помощью системного sharing-диалога. */
suspend fun Fragment.shareImage(bitmap: Bitmap, shareText: String) {
    val title = Date().time.toString() + IMAGE_TITLE_SUFFIX
    val bitmapPath: String = withContext(Dispatchers.IO) {
        MediaStore.Images.Media.insertImage(requireActivity().contentResolver, bitmap, title, null)
    }
   val bitmapUri = Uri.parse(bitmapPath)
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareText)
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

fun Fragment.openUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    intent.resolveActivity(checkNotNull(context).packageManager)?.let {
        checkNotNull(context).startActivity(intent)
    }
}

/** Позволяет сохранить изображение. */
suspend fun Fragment.saveImage(bitmap: Bitmap, saveText: String) {
    val title = Date().time.toString() + IMAGE_TITLE_SUFFIX
    withContext(Dispatchers.IO) {
        MediaStore.Images.Media.insertImage(requireContext().contentResolver, bitmap, title, null)
    }

    toast(saveText.format(title))
}

/** Позволяет поделиться ссылкой на приложение с помощью системного sharing-диалога. */
fun Fragment.shareApp(shareTitle: String, shareText: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = SHARE_LINK_TEXT_TYPE
        putExtra(Intent.EXTRA_TEXT, shareText)
    }
    requireActivity().startActivity(
        Intent.createChooser(intent, shareTitle).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    )
}
