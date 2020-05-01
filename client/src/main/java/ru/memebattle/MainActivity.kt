package ru.memebattle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import ru.memebattle.feature.fatal.FatalDialogFragment
import ru.memebattle.feature.fatal.FatalViewModel
import java.io.IOException
import androidx.lifecycle.observe
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private var isFatalErrorCatch = false
    private val fatalViewModel: FatalViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fatalViewModel.closeDialogEvent.observe(this) {
            findNavController(this@MainActivity, R.id.host_global)
                .navigate(R.id.mainFragment)
        }
        catchFuckingException()
    }

    private fun catchFuckingException() {
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            if (e !is IOException) exitProcess(0)
            if (isFatalErrorCatch) return@setDefaultUncaughtExceptionHandler
            lifecycleScope.launch {
                FatalDialogFragment().show(supportFragmentManager, null)
                isFatalErrorCatch = false
            }
            isFatalErrorCatch = true
        }
    }
}
