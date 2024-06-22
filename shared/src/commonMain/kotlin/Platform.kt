interface Platform {
    val name: String
    val host: String
}

expect fun getPlatform(): Platform