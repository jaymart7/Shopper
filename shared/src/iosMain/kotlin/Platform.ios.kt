import org.koin.core.module.Module
import org.koin.dsl.module
import repository.FakeSessionRepositoryImpl
import repository.SessionRepository

actual fun platformModule(): Module {
    return module {
        single<SessionRepository> {
            FakeSessionRepositoryImpl()
        }

        single<Platform> {
            TODO("")
            /*object : Platform {
                override val name: String = UIDevice.currentDevice.systemName() +
                        " " + UIDevice.currentDevice.systemVersion
                override val host: String = "localhost"
            }*/
        }
    }
}