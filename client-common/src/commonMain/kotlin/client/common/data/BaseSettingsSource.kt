package client.common.data

import client.common.data.settings.boolean

interface BaseSettingsSource {
    var notFirstLaunch: Boolean
}

class BaseSettingsSourceImpl(val settings: Settings) : BaseSettingsSource {
    override var notFirstLaunch: Boolean by settings.boolean()
}