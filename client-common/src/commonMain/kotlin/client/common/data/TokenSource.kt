package client.common.data

import client.common.data.settings.boolean
import client.common.data.settings.string

interface TokenSource {

    var token: String?
    var hasWin: Boolean
}

class SettingsTokenSource(settings: Settings): TokenSource {

    override var token: String? by settings.string()
    override var hasWin: Boolean by settings.boolean()
}