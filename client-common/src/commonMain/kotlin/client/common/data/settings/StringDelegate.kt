package client.common.data.settings

import client.common.data.Settings
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun Settings.string(): ReadWriteProperty<Any, String?> = StringDelegate(this)

private class StringDelegate(private val settings: Settings) : ReadWriteProperty<Any, String?> {

    override fun getValue(thisRef: Any, property: KProperty<*>): String? =
        settings.getString(property.name)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        settings.setString(property.name, value)
    }
}