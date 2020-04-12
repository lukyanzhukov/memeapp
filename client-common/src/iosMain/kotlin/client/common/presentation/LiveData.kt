package client.common.presentation

actual abstract class LiveData<T> {
    actual open val value: T?
        get() = null

    abstract fun observe(observer: (T) -> Unit)
    abstract fun removeObserver(observer: (T) -> Unit)
}