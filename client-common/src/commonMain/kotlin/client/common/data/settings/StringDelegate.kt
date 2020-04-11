package client.common.data.settings

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun Settings.string(): ReadWriteProperty<Any, String?> = StringDelegate(this)

private class StringDelegate(private val settings: Settings) : ReadWriteProperty<Any, String?> {

    override fun getValue(thisRef: Any, property: KProperty<*>): String? =
        settings.getStringOrNull(property.name)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        settings[property.name] = value
    }
}