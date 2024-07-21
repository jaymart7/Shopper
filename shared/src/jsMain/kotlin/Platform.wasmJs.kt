import org.koin.core.module.Module
import org.koin.dsl.module
import repository.FakeSessionRepositoryImpl
import repository.SessionRepository

class WasmPlatform : Platform {
    override val name: String = "Web with Kotlin/Wasm"
    override val host: String = "http://localhost"
}

actual fun platformModule(): Module =
    module {
        single<Platform> { WasmPlatform() }

        single<SessionRepository> {
            FakeSessionRepositoryImpl()
        }
    }