package client.common.presentation

expect abstract class LiveData<T> {
    
    open val value: T?
}