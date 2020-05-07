package client.common.presentation

expect class SingleLiveEvent<T>() : LiveData<T> {

    override var value: T?
}

fun <T> SingleLiveEvent<T>.call() {
    value = null
}