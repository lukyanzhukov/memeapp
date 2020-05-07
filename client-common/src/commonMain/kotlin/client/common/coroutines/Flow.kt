package client.common.coroutines

import client.common.presentation.LiveData
import client.common.presentation.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

@UseExperimental(InternalCoroutinesApi::class)
suspend inline fun <T> Flow<T>.collect(crossinline collector: (T) -> Unit) =
    collect(object : FlowCollector<T> {
        override suspend fun emit(value: T) {
            collector(value)
        }
    })

internal fun <T> Flow<T>.asLiveData(scope: CoroutineScope): LiveData<T> {
    val liveData = MutableLiveData<T>()

    scope.launch {
        collect {
            liveData.value = it
        }
    }

    return liveData
}