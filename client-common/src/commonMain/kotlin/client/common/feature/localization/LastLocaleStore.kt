package client.common.feature.localization

import client.common.data.Settings
import client.common.data.settings.string

class LastLocaleStore(settings: Settings) {

    var lastLanguage by settings.string()
    var lastCountry by settings.string()
}