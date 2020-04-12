package client.common.data

import com.russhwolf.settings.Settings
import client.common.data.settings.string

interface LoginSource {

    var login: String?
}

class SettingsLoginSource(settings: Settings): LoginSource {

    override var login: String? by settings.string()
}