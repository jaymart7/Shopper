package ui.productdetails

import com.arkivanov.decompose.value.Value
import model.Product

internal interface ProductDetailsComponent {
    val model: Value<Model>

    data class Model(
        val product: Product,
        val isLoading: Boolean = false
    )

    fun handleEvent(event: ProductDetailsEvent)
}

internal sealed class ProductDetailsEvent {
    data class UpdateTitle(val title: String) : ProductDetailsEvent()
    data object OnUpdate : ProductDetailsEvent()
    data object OnDelete : ProductDetailsEvent()
}

internal sealed class ProductOperation {
    data object Update : ProductOperation()
    data object Delete : ProductOperation()
}