package client.common.feature.rating

import client.common.data.getRating
import client.common.presentation.LiveData
import client.common.presentation.MutableLiveData
import client.common.presentation.ViewModel
import client.common.presentation.viewModelScope
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch

class RatingViewModel(private val httpClient: HttpClient) : ViewModel() {

    private val _state = MutableLiveData<RatingState>()
    val state: LiveData<RatingState>
        get() = _state

    fun getRating(name: String) {
        viewModelScope.launch {
            try {
                _state.value = RatingState.Progress
                val rating = httpClient.getRating(name)
                print(rating)
                _state.value = RatingState.Success(rating)
            } catch (e: Exception) {
                _state.value = RatingState.Fail
            }
        }
    }
}