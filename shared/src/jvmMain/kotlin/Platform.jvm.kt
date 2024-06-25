import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.koin.core.module.Module
import org.koin.dsl.module
import ph.mart.shopper.db.ShopperDatabase

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val host: String = "http://localhost"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun platformModule(): Module {
    return module {
        single<SqlDriver> {
            val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
            ShopperDatabase.Schema.create(driver)
            driver
        }
    }
}