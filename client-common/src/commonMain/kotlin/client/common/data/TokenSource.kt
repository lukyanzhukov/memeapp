package client.common.data

import client.common.data.settings.string

interface TokenSource {

    var token: String?
}

class SettingsTokenSource(settings: Settings): TokenSource {

    override var token: String? by settings.string()
}