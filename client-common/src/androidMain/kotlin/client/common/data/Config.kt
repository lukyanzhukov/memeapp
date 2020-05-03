package client.common.data

import ru.memebattle.client.common.BuildConfig

actual fun baseUrl() = BuildConfig.BASE_URL

actual fun socketHost() = BuildConfig.SOCKET_HOST

actual fun socketPort(): Int = BuildConfig.SOCKET_PORT