package ru.memebattle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.memebattle.feature.fatal.FatalDialogFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private var isFatalErrorCatch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        catchFuckingException()
    }

    private fun catchFuckingException() {
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            if (isFatalErrorCatch) return@setDefaultUncaughtExceptionHandler
            lifecycleScope.launch {
                FatalDialogFragment().show(supportFragmentManager, null)
                isFatalErrorCatch = false
            }
            isFatalErrorCatch = true
        }
    }
}
