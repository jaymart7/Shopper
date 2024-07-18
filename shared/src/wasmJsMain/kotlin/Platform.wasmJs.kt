import org.koin.core.module.Module

class WasmPlatform : Platform {
    override val name: String = "Web with Kotlin/Wasm"
    override val host: String = "http://localhost"
}

actual fun platformModule(): Module {
    TODO("Not yet implemented")
}