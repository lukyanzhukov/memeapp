package client.common.presentation

actual class SingleLiveEvent<T> : LiveData<T>() {

    private var observer: ((T) -> Unit)? = null

    actual override var value: T? = null
        set(value) {
            field = value
            notify(value)
        }

    override fun observe(observer: (T) -> Unit) {
        this.observer = observer
        notify(value)
    }

    private fun notify(value: T?) {
        value ?: return

        observer?.also {
            it(value)
            this.value = null
        }
    }

    override fun removeObserver(observer: (T) -> Unit) {
        this.observer = null
    }
}