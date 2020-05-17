package client.common.feature.splash

import client.common.data.getLocale
import client.common.feature.localization.DeviceLocaleSource
import client.common.feature.localization.LastLocaleStore
import client.common.presentation.*
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import locale.LocaleQueries

class SplashViewModel(
    private val localeQueries: LocaleQueries,
    private val client: HttpClient,
    private val deviceLocaleSource: DeviceLocaleSource,
    private val lastLocaleStore: LastLocaleStore
) : ViewModel() {

    private val _navigation = SingleLiveEvent<Unit>()
    val navigation: LiveData<Unit>
        get() = _navigation

    private val _state = MutableLiveData<SplashState>()
    val state: LiveData<SplashState>
        get() = _state

    init {
        loadLocale()
    }

    fun loadLocale() {
        viewModelScope.launch {

            try {
                val localeExists = withContext(Dispatchers.Default) {
                    localeQueries.getCount().executeAsOne() != 0L
                }

                if (!localeExists or setDifferentLanguage()) {
                    _state.value = SplashState.Progress

                    val locale = client.getLocale(
                        language = deviceLocaleSource.getLanguage(),
                        country = deviceLocaleSource.getCountry()
                    )

                    lastLocaleStore.lastCountry = deviceLocaleSource.getCountry()
                    lastLocaleStore.lastLanguage = deviceLocaleSource.getLanguage()

                    withContext(Dispatchers.Default) {
                        localeQueries.transaction {
                            locale.forEach {
                                localeQueries.insertLocale(it.key.name, it.value)
                            }
                        }
                    }

                    _navigation.call()
                } else {
                    _navigation.call()
                }
            } catch (e: Exception) {
                _state.value = SplashState.Error
            }
        }
    }

    private fun setDifferentLanguage(): Boolean =
        (lastLocaleStore.lastCountry != deviceLocaleSource.getCountry()) or
                (lastLocaleStore.lastLanguage != deviceLocaleSource.getLanguage())
}