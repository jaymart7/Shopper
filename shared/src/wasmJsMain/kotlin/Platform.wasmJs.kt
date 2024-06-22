class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
    override val host: String = "http://localhost"
}

actual fun getPlatform(): Platform = WasmPlatform()