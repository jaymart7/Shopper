package di

import getPlatform
import io.ktor.client.HttpClient
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
import org.koin.dsl.module
import repository.SessionRepository

val networkModule = module {
    single<HttpClient> {
        createHttpClient(
            sessionRepository = get()
        )
    }
}

private fun createHttpClient(
    sessionRepository: SessionRepository
): HttpClient {
    return HttpClient {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        HttpResponseValidator {
            validateResponse { response ->
                if (response.status.value == 401) {
                    sessionRepository.clearSession()
                    // TODO navigate to login screen
                }
            }
        }

        defaultRequest {
            url {
                protocol = URLProtocol.HTTP
                port = 8080
                host = getPlatform().host
            }
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer ${sessionRepository.getToken().orEmpty()}")
        }

        install(ContentNegotiation) {
            json(Json)
        }
    }
}