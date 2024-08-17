import org.koin.core.module.Module

interface Platform {
    val name: String
    val remoteHost: String
    val localHost: String
}

expect fun platformModule(): Module