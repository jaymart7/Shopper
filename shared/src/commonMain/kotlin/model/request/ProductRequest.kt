package model.request

import kotlinx.serialization.Serializable

@Serializable
internal data class ProductRequest(
    val title: String
)