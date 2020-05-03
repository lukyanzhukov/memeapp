package client.common.data

import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.features.websocket.ClientWebSocketSession
import io.ktor.client.features.websocket.DefaultClientWebSocketSession
import io.ktor.client.features.websocket.webSocket
import io.ktor.client.request.ClientUpgradeContent
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.statement.HttpResponseContainer
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.http.*
import io.ktor.http.cio.websocket.DefaultWebSocketSession
import io.ktor.http.cio.websocket.FrameType
import io.ktor.http.cio.websocket.WebSocketSession
import io.ktor.http.websocket.websocketServerAccept
import io.ktor.util.AttributeKey
import io.ktor.util.InternalAPI
import io.ktor.util.encodeBase64
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.writeText

/**
 * Client WebSocket feature.
 *
 * @property pingInterval - interval between [FrameType.PING] messages.
 * @property maxFrameSize - max size of single websocket frame.
 */
class WebSockets(
    val pingInterval: Long = -1L,
    val maxFrameSize: Long = Int.MAX_VALUE.toLong()
) {
    @Suppress("KDocMissingDocumentation")
    companion object Feature : HttpClientFeature<Unit, WebSockets> {
        override val key: AttributeKey<WebSockets> = AttributeKey("Websocket")

        override fun prepare(block: Unit.() -> Unit): WebSockets = WebSockets()

        override fun install(feature: WebSockets, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Render) {
                if (!context.url.protocol.isWebsocket()) return@intercept

                proceedWith(WebSocketContent())
            }

            scope.responsePipeline.intercept(HttpResponsePipeline.Transform) { (info, session) ->
                if (session !is WebSocketSession) return@intercept
                if (info.type == DefaultClientWebSocketSession::class) {
                    val clientSession: DefaultClientWebSocketSession = with(feature) {
                        DefaultClientWebSocketSession(context, session.asDefault())
                    }

                    proceedWith(HttpResponseContainer(info, clientSession))
                    return@intercept
                }

                val response = HttpResponseContainer(info, DelegatingClientWebSocketSession(context, session))
                proceedWith(response)
            }
        }
    }

    private fun WebSocketSession.asDefault(): DefaultWebSocketSession {
        if (this is DefaultWebSocketSession) return this
        return DefaultWebSocketSession(this, pingInterval, maxFrameSize)
    }
}

@Suppress("KDocMissingDocumentation")
class WebSocketException(message: String) : IllegalStateException(message)

class DelegatingClientWebSocketSession(
    override val call: HttpClientCall, session: WebSocketSession
) : ClientWebSocketSession, WebSocketSession by session

private const val WEBSOCKET_VERSION = "13"
private const val NONCE_SIZE = 16

@UseExperimental(InternalAPI::class)
class WebSocketContent : ClientUpgradeContent() {
    private val nonce: String = buildString {
        val nonce = generateNonce(NONCE_SIZE)
        append(nonce.encodeBase64())
    }

    override val headers: Headers = HeadersBuilder().apply {
        append(HttpHeaders.Upgrade, "websocket")
        append(HttpHeaders.Connection, "upgrade")

        append(HttpHeaders.SecWebSocketKey, nonce)
        append(HttpHeaders.SecWebSocketVersion, WEBSOCKET_VERSION)
    }.build()

    override fun verify(headers: Headers) {
        val serverAccept = headers[HttpHeaders.SecWebSocketAccept]
            ?: error("Server should specify header ${HttpHeaders.SecWebSocketAccept}")

        val expectedAccept = websocketServerAccept(nonce)
        check(expectedAccept == serverAccept) {
            "Failed to verify server accept header. Expected: $expectedAccept, received: $serverAccept"
        }
    }

    override fun toString(): String = "WebSocketContent"
}

fun generateNonce(size: Int): ByteArray = buildPacket {
    while (this.size < size) {
        writeText(generateNonce())
    }
}.readBytes(size)

expect fun generateNonce(): String

suspend fun HttpClient.memeSocket(
    method: HttpMethod = HttpMethod.Get, host: String = "localhost", port: Int = DEFAULT_PORT, path: String = "/", protocol: URLProtocol,
    request: HttpRequestBuilder.() -> Unit = {}, block: suspend DefaultClientWebSocketSession.() -> Unit
): Unit = webSocket(
    method, host, port, path, request = {
        url.protocol = protocol
        url.port = port

        request()
    }, block = block
)