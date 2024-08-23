import org.koin.core.module.Module
import org.koin.dsl.module
import repository.SessionRepository

class WasmPlatform : Platform {
    override val name: String = "Web with Kotlin/Wasm"
    override val localHost: String = "localhost"
    override val remoteHost: String = "shopperserver-heec.onrender.com"
}

actual fun platformModule(): Module =
    module {
        single<Platform> { WasmPlatform() }

        single<SessionRepository> {
            SessionRepositoryImpl()
        }
    }