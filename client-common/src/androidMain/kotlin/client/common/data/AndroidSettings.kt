package client.common.data

import android.content.SharedPreferences
import androidx.core.content.edit

class AndroidSettings(private val preferences: SharedPreferences) : Settings {
    override fun setString(key: String, value: String?) =
        preferences.edit {
            putString(key, value)
        }

    override fun getString(key: String): String? =
        preferences.getString(key, null)

    override fun setBoolean(key: String, value: Boolean) {
        preferences.edit {
            putBoolean(key, value)
        }
    }

    override fun getBoolean(key: String): Boolean? = preferences.getBoolean(key, false)
}