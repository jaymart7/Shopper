package ph.mart.shopper.model.response

import kotlinx.serialization.Serializable

@Serializable
internal data class AccountResponse(
    val name: String,
    val username: String
)