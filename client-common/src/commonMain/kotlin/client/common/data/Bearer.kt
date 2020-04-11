package client.common.data

import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.AuthProvider
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.http.auth.HttpAuthHeader

fun Auth.bearer(tokenSource: TokenSource) {
    providers.add(Bearer(tokenSource))
}

private class Bearer(private val tokenSource: TokenSource) : AuthProvider {

    override val sendWithoutRequest: Boolean = false

    override suspend fun addRequestHeaders(request: HttpRequestBuilder) {
        request.headers {
            append("Authorization", "Bearer ${tokenSource.token}")
        }
    }

    override fun isApplicable(auth: HttpAuthHeader): Boolean =
        !tokenSource.token.isNullOrBlank()
}