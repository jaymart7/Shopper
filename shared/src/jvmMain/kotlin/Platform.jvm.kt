class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val host: String = "http://localhost"
}

actual fun getPlatform(): Platform = JVMPlatform()