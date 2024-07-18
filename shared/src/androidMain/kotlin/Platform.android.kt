import android.os.Build
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.koin.dsl.module
import ph.mart.shopper.db.ShopperDatabase

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val host: String = "10.0.2.2"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun platformModule() = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = ShopperDatabase.Schema,
            context = get(),
            name = "shopper.db",
            callback = object : AndroidSqliteDriver.Callback(ShopperDatabase.Schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    db.setForeignKeyConstraintsEnabled(true) // enabling foreign key constraints for the Android SQLite driver.
                }
            })
    }
}