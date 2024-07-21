package di

import org.koin.dsl.module
import platformModule
import repository.AccountRepository
import repository.AccountRepositoryImpl
import repository.ProductRepository
import repository.ProductRepositoryImpl

fun appModule() = listOf(commonModule, platformModule(), networkModule)

val commonModule = module {
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