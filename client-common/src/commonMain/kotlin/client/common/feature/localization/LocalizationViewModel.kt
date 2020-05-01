package client.common.feature.localization

import client.common.coroutines.asLiveData
import client.common.presentation.ViewModel
import client.common.presentation.viewModelScope
import kotlinx.coroutines.flow.receiveAsFlow

class LocalizationViewModel(localizationDelegate: LocalizationDelegate) : ViewModel() {

    val locale = localizationDelegate.localeChannel.openSubscription()
        .receiveAsFlow()
        .asLiveData(viewModelScope)
}