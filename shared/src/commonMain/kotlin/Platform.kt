import org.koin.core.module.Module

interface Platform {
    val name: String
    val host: String
}

expect fun platformModule(): Module