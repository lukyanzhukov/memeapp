package client

import client.common.UIDispatcher
import kotlinx.coroutines.CoroutineDispatcher

actual fun uiDispatcher(): CoroutineDispatcher = UIDispatcher