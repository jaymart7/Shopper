package model.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val name: String,
    val username: String
)