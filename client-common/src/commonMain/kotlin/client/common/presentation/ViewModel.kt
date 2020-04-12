package client.common.presentation

import kotlinx.coroutines.CoroutineScope

expect open class ViewModel()

expect val ViewModel.viewModelScope: CoroutineScope