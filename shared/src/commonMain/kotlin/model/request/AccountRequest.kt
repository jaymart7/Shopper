package model.request

import kotlinx.serialization.Serializable

@Serializable
data class AccountRequest(
    val name: String,
    val username: String,
    val password: String
)