package client.common.presentation

import androidx.lifecycle.LiveData as AndroidxLiveData

actual abstract class LiveData<T> {

    abstract val platform: AndroidxLiveData<T>

    actual open val value: T?
        get() = platform.value
}