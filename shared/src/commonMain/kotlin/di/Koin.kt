package di

import org.koin.dsl.module
import ph.mart.shopper.db.ShopperDatabase
import platformModule
import repository.AccountRepository
import repository.AccountRepositoryImpl
import repository.ProductRepository
import repository.ProductRepositoryImpl
import repository.SessionRepository
import repository.SessionRepositoryImpl

fun appModule() = listOf(commonModule, platformModule(), networkModule)

val commonModule = module {
    single<ShopperDatabase> {
        ShopperDatabase(
            driver = get()
        )
    }

    single<SessionRepository> {
        SessionRepositoryImpl(
            shopperDatabase = get()
        )
    }

    single<AccountRepository> {
        AccountRepositoryImpl(
            sessionRepository = get(),
            httpClient = get()
        )
    }

    single<ProductRepository> {
        ProductRepositoryImpl(
            httpClient = get()
        )
    }
}