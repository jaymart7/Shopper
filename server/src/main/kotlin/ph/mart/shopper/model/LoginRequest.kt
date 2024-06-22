package ph.mart.shopper.model

import kotlinx.serialization.Serializable

@Serializable
internal data class LoginRequest(
    val username: String,
    val password: String
)