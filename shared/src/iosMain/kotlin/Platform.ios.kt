import app.cash.sqldelight.db.SqlDriver
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.UIKit.UIDevice

actual fun platformModule(): Module {
    return module {
        single<SqlDriver> {
            TODO("")
        }

        single<Platform> {
            object : Platform {
                override val name: String = UIDevice.currentDevice.systemName() +
                        " " + UIDevice.currentDevice.systemVersion
                override val host: String = "localhost"
            }
        }
    }
}