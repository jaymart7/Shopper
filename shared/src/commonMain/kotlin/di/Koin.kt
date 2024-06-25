package di

import common.Home
import common.HomeImpl
import common.Login
import common.LoginImpl
import common.ScreenNavigator
import common.ScreenNavigatorImpl
import org.koin.dsl.module
import ph.mart.shopper.db.ShopperDatabase
import platformModule
import repository.AccountRepository
import repository.AccountRepositoryImpl
import repository.SessionRepository
import repository.SessionRepositoryImpl

fun appModule() = listOf(commonModule, platformModule(), networkModule)

val commonModule = module {
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

    single<Login> { params ->
        LoginImpl(
            accountRepository = get(),
            screenNavigator = get(),
            scope = params.get()
        )
    }

    single<ScreenNavigator> { params ->
        ScreenNavigatorImpl(
            accountRepository = get(),
            scope = params.get()
        )
    }

    single<Home> { params ->
        HomeImpl(
            accountRepository = get(),
            screenNavigator = get(),
            scope = params.get()
        )
    }

    single<ShopperDatabase> {
        ShopperDatabase(
            driver = get()
        )
    }
}