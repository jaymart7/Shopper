package model.response

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    val code: String,
    override val message: String?
) : Exception()