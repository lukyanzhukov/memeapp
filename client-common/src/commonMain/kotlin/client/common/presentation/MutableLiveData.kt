package client.common.presentation

expect class MutableLiveData<T>(initialValue: T? = null): LiveData<T> {

    override var value: T?
}