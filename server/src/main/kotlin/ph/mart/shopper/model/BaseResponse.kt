package ph.mart.shopper.model

import kotlinx.serialization.Serializable

@Serializable
internal data class BaseResponse<T>(
    val data: T?,
    val message: String
)