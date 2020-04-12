package client.common.presentation

actual class SingleLiveEvent<T> : LiveData<T>() {

    private val _platform = PlatformSingleLiveEvent<T>()
    override val platform: androidx.lifecycle.LiveData<T>
        get() = _platform

    actual override var value: T?
        get() = _platform.value
        set(value) {
            _platform.value = value
        }
}