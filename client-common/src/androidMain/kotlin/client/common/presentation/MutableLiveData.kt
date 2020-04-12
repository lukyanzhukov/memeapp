package client.common.presentation

import androidx.lifecycle.MutableLiveData as AndroidxMutableLiveData

actual class MutableLiveData<T> actual constructor(
    initialValue: T?
) : LiveData<T>() {

    override val platform: AndroidxMutableLiveData<T> =
        initialValue?.let(::AndroidxMutableLiveData) ?: AndroidxMutableLiveData()

    actual override var value: T?
        get() = platform.value
        set(value) {
            platform.value = value
        }
}