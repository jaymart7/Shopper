package repository

import di.json
import di.url
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import model.request.LoginRequest
import model.response.AccountResponse
import model.response.TokenResponse

interface AccountRepository {

    suspend fun login(username: String, password: String)

    suspend fun getAccount(): AccountResponse

    fun logout()

    fun hasToken(): Boolean
}

class AccountRepositoryImpl(
    private val sessionRepository: SessionRepository,
    private val httpClient: HttpClient
) : AccountRepository {

    override suspend fun login(
        username: String,
        password: String
    ) {
        val response: HttpResponse = httpClient.post("$url/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(username, password))
        }
        val token = json.decodeFromString<TokenResponse>(response.bodyAsText()).token
        sessionRepository.setToken(token)
    }

    override suspend fun getAccount(): AccountResponse {
        val token = sessionRepository.getToken()
        val response = httpClient.get("$url/account") {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer $token")
        }

        return json.decodeFromString(response.bodyAsText())
    }

    override fun logout() {
        sessionRepository.clearSession()
    }

    override fun hasToken(): Boolean {
        return sessionRepository.getToken() != null
    }

}