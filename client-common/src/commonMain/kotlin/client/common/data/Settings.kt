package client.common.data

interface Settings {

    fun setString(key: String, value: String?)
    fun getString(key: String): String?
}