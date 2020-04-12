package client.common.presentation

import kotlinx.coroutines.CoroutineScope
import androidx.lifecycle.viewModelScope as androidxViewModelScope

actual open class ViewModel: androidx.lifecycle.ViewModel()

actual val ViewModel.viewModelScope: CoroutineScope
    get() = androidxViewModelScope