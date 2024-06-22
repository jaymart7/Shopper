package ph.mart.shopper.model

import kotlinx.serialization.Serializable

@Serializable
internal data class Customer(
    val id: String,
    var firstName: String,
    var lastName: String,
    var email: String
)