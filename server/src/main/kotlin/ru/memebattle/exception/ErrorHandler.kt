package ru.memebattle.exception

import io.ktor.application.call
import io.ktor.features.ParameterConversionException
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond

object ErrorHandler {

    @Suppress("EXPERIMENTAL_API_USAGE")
    fun setup(configuration: StatusPages.Configuration) {
        with(configuration) {
            exception<NotImplementedError> { error ->
                call.respond(HttpStatusCode.NotImplemented)
                throw error
            }
            exception<ParameterConversionException> { error ->
                call.respond(HttpStatusCode.BadRequest)
                throw error
            }
            exception<UserExistsException> { error ->
                call.respond(HttpStatusCode.BadRequest)
                throw error
            }
            exception<InvalidPasswordException> { error ->
                call.respond(HttpStatusCode.Forbidden)
                throw error
            }
        }
    }
}