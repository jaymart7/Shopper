import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
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
            val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
            ShopperDatabase.Schema.create(driver)
            driver
        }

        single<Platform> {
            object : Platform {
                override val name: String = "Java ${System.getProperty("java.version")}"
                override val host: String = "localhost"
            }
        }
    }
}