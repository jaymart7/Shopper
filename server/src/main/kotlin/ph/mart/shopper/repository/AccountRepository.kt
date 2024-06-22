package ph.mart.shopper.repository

import ph.mart.shopper.model.LoginRequest
import ph.mart.shopper.model.mapper.toAccount
import ph.mart.shopper.model.mapper.toAccountResponse
import ph.mart.shopper.model.presentation.Account
import ph.mart.shopper.model.request.AccountRequest
import ph.mart.shopper.model.response.AccountResponse
import java.util.*

internal val accountStorage = mutableListOf<Account>()

internal interface AccountRepository {

    fun login(loginRequest: LoginRequest): AccountResponse?

    fun addAccount(accountRequest: AccountRequest)

    fun getAccount(username: String): AccountResponse?
}

internal class AccountRepositoryImpl : AccountRepository {

    override fun addAccount(accountRequest: AccountRequest) {
        val id = Random().nextInt(1000)
        accountStorage.add(accountRequest.toAccount(id))
    }

    override fun login(loginRequest: LoginRequest): AccountResponse? =
        accountStorage.find { account ->
            account.username == loginRequest.username && account.password == loginRequest.password
        }?.toAccountResponse()

    override fun getAccount(username: String): AccountResponse? {
        return accountStorage.find { account ->
            account.username == username
        }?.toAccountResponse()
    }

}