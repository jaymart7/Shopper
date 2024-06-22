package network

import getPlatform
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val json = Json { ignoreUnknownKeys = true }

val platform = getPlatform()
val url = "${platform.host}:8080"

val httpClient = HttpClient {
    expectSuccess = true

    HttpResponseValidator {
        handleResponseExceptionWithRequest { exception, request ->
            val clientException =
                exception as? ClientRequestException ?: return@handleResponseExceptionWithRequest
            val exceptionResponse = exception.response
            val exceptionResponseText = exceptionResponse.bodyAsText()
            throw Exception(exceptionResponseText)
        }
    }

    install(ContentNegotiation) { json(Json) }

    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }
}