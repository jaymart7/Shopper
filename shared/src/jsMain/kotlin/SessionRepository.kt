import kotlinx.browser.sessionStorage
import org.w3c.dom.get
import org.w3c.dom.set
import repository.SessionRepository

class SessionRepositoryImpl : SessionRepository {

    override fun setToken(token: String) {
        sessionStorage[TOKEN] = token
    }

    override fun getToken(): String? = sessionStorage[TOKEN]

    override fun clearSession() {
        sessionStorage.clear()
    }

    companion object {
        const val TOKEN = "token"
    }
}