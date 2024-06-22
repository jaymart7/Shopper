package ph.mart.shopper.plugins


import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

internal fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}