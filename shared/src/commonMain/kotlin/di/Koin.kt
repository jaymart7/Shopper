package di

import Platform
import common.Home
import common.HomeImpl
import common.Login
import common.LoginImpl
import common.ScreenNavigator
import common.ScreenNavigatorImpl
import getPlatform
import org.koin.core.context.startKoin
import org.koin.dsl.module
import repository.AccountRepository
import repository.AccountRepositoryImpl
import repository.SessionRepository
import repository.SessionRepositoryImpl

fun appModule() = listOf(commonModule, platformModule)

val platformModule = module {
    single<Platform> { getPlatform() }
}

val commonModule = module {
    single<SessionRepository> { SessionRepositoryImpl() }

    single<AccountRepository> {
        AccountRepositoryImpl(
            sessionRepository = get()
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
}

fun initKoin() {
    startKoin {
        modules(appModule())
    }
}