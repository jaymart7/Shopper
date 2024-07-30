package ui.home

import com.arkivanov.decompose.value.Value
import model.Product
import util.ViewState

internal interface HomeComponent {
    val model: Value<Model>

    data class Model(
        val scrollTo: Int? = null,
        val productsState: ViewState<List<Product>> = ViewState.Loading
    )

    fun handleEvent(event: HomeEvent)

    fun update(updatedProduct: Product)

    fun delete(productId: Int)

    fun add(product: Product)
}

internal sealed class HomeEvent {
    data object RefreshProduct : HomeEvent()
    data object ClearScroll : HomeEvent()
    data class ProductClick(val product: Product) : HomeEvent()
}