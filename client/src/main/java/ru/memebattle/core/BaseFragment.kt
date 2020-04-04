package ru.memebattle.core

import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

open class BaseFragment : Fragment(), CoroutineScope by MainScope() {

    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }
}