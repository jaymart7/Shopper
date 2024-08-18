package ui.productdetails

import com.arkivanov.decompose.value.Value
import model.Product

interface ProductDetailsComponent {
    val model: Value<Model>

    data class Model(
        val product: Product,
        val isLoading: Boolean = false
    )

    fun handleEvent(event: ProductDetailsEvent)
}

sealed class ProductDetailsEvent {
    data class OnUpdate(val product: Product) : ProductDetailsEvent()
    data object OnDelete : ProductDetailsEvent()
}