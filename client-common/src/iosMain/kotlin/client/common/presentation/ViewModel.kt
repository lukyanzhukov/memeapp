package client.common.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

actual open class ViewModel {

    val actualScope = MainScope()

    fun clear() {
        actualScope.cancel()
    }
}

actual val ViewModel.viewModelScope: CoroutineScope
    get() = actualScope