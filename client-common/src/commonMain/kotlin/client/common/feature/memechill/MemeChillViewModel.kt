package client.common.feature.memechill

import client.common.data.getChillMemes
import client.common.presentation.LiveData
import client.common.presentation.MutableLiveData
import client.common.presentation.ViewModel
import client.common.presentation.viewModelScope
import io.ktor.client.HttpClient
import kotlinx.coroutines.launch
import ru.memebattle.common.dto.game.MemeModel

class MemeChillViewModel(private val client: HttpClient) : ViewModel() {

    val state: LiveData<MemeChillState>
        get() = _state

    private lateinit var mode: String
    private val _state = MutableLiveData<MemeChillState>()
    private var memePairs: ArrayList<Pair<MemeModel, MemeModel>> = arrayListOf()


    fun setGameMode(mode: String) {
        this.mode = mode
    }

    @ExperimentalStdlibApi
    fun getMemesPair() {
        viewModelScope.launch {
            if (memePairs.isEmpty()) {
                loadMemes()
            }
            if (memePairs.isNotEmpty()) {
                _state.value = MemeChillState.SuccessMemePair(memePairs.first())
                memePairs.removeFirst()
            }
        }
    }

    private suspend fun loadMemes() {
        try {
            _state.value = MemeChillState.Loading
            val memes = client.getChillMemes(mode)
            for (i in memes.indices step 2) {
                if (i + 1 >= memes.size) return
                memePairs.add(memes[i] to memes[i + 1])
            }
        } catch (e: Exception) {
            _state.value = MemeChillState.Error(e)
        }
    }
}