package client.common.presentation

actual class MutableLiveData<T> actual constructor(initialValue: T?) : LiveData<T>() {

    private val observers = mutableSetOf<(T) -> Unit>()

    actual override var value: T? = null
        set(value) {
            field = value
            value ?: return
            observers.forEach {
                it(value)
            }
        }

    override fun observe(observer: (T) -> Unit) {
        observers += observer
        value?.also(observer::invoke)
    }

    override fun removeObserver(observer: (T) -> Unit) {
        observers -= observer
    }
}