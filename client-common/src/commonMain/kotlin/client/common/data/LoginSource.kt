package client.common.data

import client.common.data.settings.string

interface LoginSource {

    var login: String?
}

class SettingsLoginSource(settings: Settings): LoginSource {

    override var login: String? by settings.string()
}