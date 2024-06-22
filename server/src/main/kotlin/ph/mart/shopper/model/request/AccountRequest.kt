package ph.mart.shopper.model.request

import kotlinx.serialization.Serializable

@Serializable
internal data class AccountRequest(
    val name: String,
    val username: String,
    val password: String
)