package client.common.data

import platform.Foundation.NSUserDefaults

class AppleSettings(name: String?) : Settings {

    private val userDefaults: NSUserDefaults = NSUserDefaults(suiteName = name)

    override fun setString(key: String, value: String?) {
        userDefaults.setObject(value, key)
    }

    override fun getString(key: String): String? =
        if (userDefaults.objectForKey(key) != null) {
            userDefaults.stringForKey(key)
        } else {
            null
        }
}