package client.common.presentation

import client.common.UIDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

actual open class ViewModel {

    val actualScope = CoroutineScope(UIDispatcher)

    fun clear() {
        actualScope.cancel()
    }
}

actual val ViewModel.viewModelScope: CoroutineScope
    get() = actualScope