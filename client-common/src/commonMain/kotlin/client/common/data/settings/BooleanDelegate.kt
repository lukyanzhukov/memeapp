package client.common.data.settings

import client.common.data.Settings
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun Settings.boolean(): ReadWriteProperty<Any, Boolean> = BooleanDelegate(this)

private class BooleanDelegate(private val settings: Settings) : ReadWriteProperty<Any, Boolean> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean =
        settings.getBoolean(property.name) ?: false

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        settings.setBoolean(property.name, value)
    }
}