package ph.mart.shopper.plugins

import ph.mart.shopper.model.Customer
import ph.mart.shopper.model.presentation.Account
import ph.mart.shopper.repository.AccountRepositoryImpl
import ph.mart.shopper.repository.accountStorage
import ph.mart.shopper.routes.accountRouting
import ph.mart.shopper.routes.customerRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

internal val customerStorage = mutableListOf<Customer>()
internal val json = Json { ignoreUnknownKeys = true }

internal fun Application.configureRouting() {

    val accountRepository = AccountRepositoryImpl()

    initializeDatabase()
    routing {
        customerRouting()
        accountRouting(accountRepository)
    }
}

internal fun initializeDatabase() {
    customerStorage.add(Customer("1", "Jaymart", "Araga", "jaymart@yahoo.com"))

    accountStorage.add(Account(1, "Jaymart", "a", "a"))
}