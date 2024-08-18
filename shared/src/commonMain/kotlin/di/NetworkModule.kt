package di

import Platform
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import model.response.ApiError
import org.koin.dsl.module
import repository.SessionRepository

/**
 * Config for remote and local host
 */
private const val isLocal = false

val networkModule = module {
    single<HttpClient> {
        createHttpClient(
            sessionRepository = get(),
            platform = get()
        )
    }
}

private fun createHttpClient(
    sessionRepository: SessionRepository,
    platform: Platform
): HttpClient {
    return HttpClient {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        expectSuccess = true
        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, _ ->
                when (exception) {
                    is ConnectTimeoutException -> {
                        throw Exception("Please check your network connection.")
                    }

                    is ClientRequestException -> {
                        val apiError = exception.response.body<ApiError>()
                        throw apiError
                    }

                    else -> {
                        throw Exception("Something went wrong")
                    }
                }
            }
        }

        defaultRequest {
            url {
                if (isLocal) {
                    protocol = URLProtocol.HTTP
                    host = platform.localHost
                    port = 8080
                } else {
                    protocol = URLProtocol.HTTPS
                    host = platform.remoteHost
                }
            }
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer ${sessionRepository.getToken().orEmpty()}")

        }

        install(ContentNegotiation) {
            json(Json)
        }

    }
}