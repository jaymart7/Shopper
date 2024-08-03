package repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import model.mapper.toAccount
import model.presentation.Account
import model.request.AccountRequest
import model.request.LoginRequest
import model.response.AccountResponse
import model.response.TokenResponse

interface AccountRepository {

    suspend fun signUp(accountRequest: AccountRequest)

    suspend fun login(username: String, password: String)

    suspend fun getAccount(): Account

    fun logout()

    fun hasToken(): Boolean
}

class AccountRepositoryImpl(
    private val sessionRepository: SessionRepository,
    private val httpClient: HttpClient
) : AccountRepository {

    override suspend fun signUp(accountRequest: AccountRequest) {
        httpClient.post("register") {
            setBody(accountRequest)
        }
    }

    override suspend fun login(
        username: String,
        password: String
    ) {
        val response = httpClient.post("login") {
            setBody(LoginRequest(username, password))
        }
        val token: TokenResponse = response.body()
        sessionRepository.setToken(token.token)
    }

    override suspend fun getAccount(): Account {
        val response = httpClient.get("account")
        val accountResponse: AccountResponse = response.body()
        return accountResponse.toAccount()
    }

    override fun logout() {
        sessionRepository.clearSession()
    }

    override fun hasToken(): Boolean {
        return sessionRepository.getToken() != null
    }
}