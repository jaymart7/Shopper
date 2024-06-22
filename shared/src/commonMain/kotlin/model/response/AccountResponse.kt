package model.response

import kotlinx.serialization.Serializable

@Serializable
data class AccountResponse(
    val username: String,
    val name: String
)