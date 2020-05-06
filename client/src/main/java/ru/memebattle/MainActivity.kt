package ru.memebattle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import kotlinx.coroutines.launch
import ru.memebattle.core.utils.FatalErrorDialogListener
import ru.memebattle.core.utils.openFatalErrorDialog
import java.io.IOException
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    var isFatalErrorCatch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        catchFuckingException()
        findNavController(this, R.id.host_global)
    }

    private fun catchFuckingException() {
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            if (e !is IOException) exitProcess(0)
            if (isFatalErrorCatch) return@setDefaultUncaughtExceptionHandler
            lifecycleScope.launch {
                openFatalErrorDialog(FatalErrorDialogListener {
                    findNavController(this@MainActivity, R.id.host_global)
                        .navigate(R.id.mainFragment)
                })
                isFatalErrorCatch = false
            }
            isFatalErrorCatch = true
        }
    }
}
