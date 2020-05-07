package client.common.feature.localization

interface DeviceLocaleSource {

    fun getLanguage(): String
    fun getCountry(): String
}