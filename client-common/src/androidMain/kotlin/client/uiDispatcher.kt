package client

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual fun uiDispatcher(): CoroutineDispatcher =
    Dispatchers.Main