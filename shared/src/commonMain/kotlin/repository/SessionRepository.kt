package repository


interface SessionRepository {

    fun setToken(token: String)

    fun getToken(): String?

    fun clearSession()
}

class SessionRepositoryImpl() : SessionRepository {

    override fun setToken(token: String) {
//        sessionStorage[TOKEN] = token
    }

    override fun getToken(): String? = "sd"

    override fun clearSession() {
//        sessionStorage.clear()
    }

    companion object {
        const val TOKEN = "token"
    }
}