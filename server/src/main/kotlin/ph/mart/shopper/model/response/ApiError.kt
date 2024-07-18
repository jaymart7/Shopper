package ph.mart.shopper.model.response

import kotlinx.serialization.Serializable

@Serializable
internal data class ApiError(
    val code: String,
    override val message: String?
) : Exception()