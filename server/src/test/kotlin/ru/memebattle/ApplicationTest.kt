package ru.memebattle

import com.jayway.jsonpath.JsonPath
import io.ktor.application.Application
import io.ktor.config.MapApplicationConfig
import io.ktor.http.*
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import org.junit.jupiter.api.Test
import org.testcontainers.containers.PostgreSQLContainer
import ru.memebattle.common.dto.game.GameState
import kotlin.test.assertEquals

@Suppress("EXPERIMENTAL_API_USAGE")
class ApplicationTest {
    class AppPostgreSQLContainer : PostgreSQLContainer<AppPostgreSQLContainer>("postgres:latest")

    private val jsonContentType = ContentType.Application.Json.withCharset(Charsets.UTF_8)
    private val postgresContainer = AppPostgreSQLContainer().apply {
        start()
    }
    private val configure: Application.() -> Unit = {
        (environment.config as MapApplicationConfig).apply {
            put(
                "db.jdbcUrl",
                "postgres://${postgresContainer.username}:${postgresContainer.password}@${postgresContainer.containerIpAddress}:${postgresContainer.getMappedPort(
                    PostgreSQLContainer.POSTGRESQL_PORT
                )}/${postgresContainer.databaseName}"
            )
        }
        module()
    }

    @Test
    fun `unauthorized EXPECT 401 status code`() {
        withTestApplication(configure) {
            with(handleRequest(HttpMethod.Get, "/api/v1/posts")) {
                assertEquals(HttpStatusCode.Unauthorized, response.status())
            }
        }
    }

    @Test
    fun `register and me EXPECT username`() {
        withTestApplication(configure) {
            val token = register()

            with(handleRequest(HttpMethod.Get, "/api/v1/me") {
                addHeader(HttpHeaders.Authorization, "Bearer $token")
            }) {
                val username = JsonPath.read<String>(response.content!!, "$.username")
                assertEquals("vasya", username)
            }
        }
    }

    @Test
    fun `auth and me EXPECT username`() {
        withTestApplication(configure) {
            register()

            val token = with(handleRequest(HttpMethod.Post, "/api/v1/authentication") {
                addHeader(HttpHeaders.ContentType, jsonContentType.toString())
                setBody(
                    """
                        {
                            "username": "vasya",
                            "password": "password"
                        }
                    """.trimIndent()
                )
            }) {
                JsonPath.read<String>(response.content!!, "$.token")
            }

            with(handleRequest(HttpMethod.Get, "/api/v1/me") {
                addHeader(HttpHeaders.Authorization, "Bearer $token")
            }) {
                val username = JsonPath.read<String>(response.content!!, "$.username")
                assertEquals("vasya", username)
            }
        }
    }

    @Test
    fun `first connect EXPECT state START`() {
        withTestApplication(configure) {
            val token = register()

            handleWebSocketConversation(uri = "/api/v1/", setup = {
                addHeader(HttpHeaders.Authorization, "Bearer $token")
            }, callback = { incoming, _ ->
                val response = (incoming.receive() as Frame.Text).readText()
                val state = JsonPath.read<String>(response, "$.state")
                assertEquals(state, GameState.START.name)
            })
        }
    }

    private fun TestApplicationEngine.register(): String =
        handleRequest(HttpMethod.Post, "/api/v1/registration") {
            addHeader(HttpHeaders.ContentType, jsonContentType.toString())
            setBody(
                """
                        {
                            "username": "vasya",
                            "password": "password"
                        }
                    """.trimIndent()
            )
        }.let {
            JsonPath.read(it.response.content!!, "$.token")
        }
}