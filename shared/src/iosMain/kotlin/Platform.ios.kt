import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import org.koin.core.module.Module
import org.koin.dsl.module
import ph.mart.shopper.db.ShopperDatabase
import repository.SessionRepository
import repository.SessionRepositoryImpl

actual fun platformModule(): Module {
    return module {
        single<SessionRepository> {
            SessionRepositoryImpl(
                shopperDatabase = get()
            )
        }

        single<ShopperDatabase> {
            ShopperDatabase(
                driver = get<SqlDriver>()
            )
        }

        single<SqlDriver> {
            NativeSqliteDriver(ShopperDatabase.Schema, "shopper.db")
        }

        single<Platform> {
            object : Platform {
                override val name: String = "iOS"
                override val localHost: String = "localhost"
                override val remoteHost: String = "shopperserver-heec.onrender.com"
            }
        }
    }
}