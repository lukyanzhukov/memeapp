package client.common.feature.localization

import client.uiDispatcher
import com.squareup.sqldelight.Query
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import locale.LocaleQueries
import ru.memebattle.common.feature.localization.Localization

class LocalizationDelegate(private val localeQueries: LocaleQueries) {

    private val scope = CoroutineScope(uiDispatcher() + SupervisorJob())

    init {
        loadLocale()

        localeQueries.getLocale().addListener(
            object : Query.Listener {
                override fun queryResultsChanged() {
                    loadLocale()
                }
            }
        )
    }

    private fun loadLocale() {
        scope.launch {
            withContext(Dispatchers.Default) {
                localeQueries.getLocale().executeAsList()
            }.map {
                Localization.valueOf(it.key) to it.value
            }.toMap().let(localeChannel::offer)
        }
    }

    val localeChannel = ConflatedBroadcastChannel<Map<Localization, String>>()
}