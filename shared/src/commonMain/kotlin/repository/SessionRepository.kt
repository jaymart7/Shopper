package repository

import ph.mart.shopper.db.ShopperDatabase

interface SessionRepository {

    fun setToken(token: String)

    fun getToken(): String?

    fun clearSession()
}

internal class SessionRepositoryImpl(
    shopperDatabase: ShopperDatabase
) : SessionRepository {

    private val queries = shopperDatabase.shopperDatabaseQueries

    override fun setToken(token: String) {
        queries.insertItem(token)
    }

    override fun getToken(): String? {
        return queries.selectAll().executeAsOneOrNull()?.token
    }

    override fun clearSession() {
        queries.deleteAll()
    }
}