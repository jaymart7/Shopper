package component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import component.ProductDetailsComponent.Model
import kotlinx.coroutines.launch
import model.Product
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repository.ProductRepository

interface ProductDetailsComponent {
    val model: Value<Model>

    data class Model(
        val product: Product,
        val isLoading: Boolean = false
    )

    fun handleEvent(event: ProductDetailsEvent)
}

sealed class ProductDetailsEvent {
    data class UpdateTitle(val title: String) : ProductDetailsEvent()
    data object Update : ProductDetailsEvent()
    data object Delete : ProductDetailsEvent()
}

internal class DefaultProductDetailsComponent(
    componentContext: ComponentContext,
    selectedProduct: Product,
    private val onUpdated: (Product) -> Unit,
    private val onDeleted: (Int) -> Unit
) : ComponentContext by componentContext, ProductDetailsComponent, KoinComponent {

    private val productRepository: ProductRepository by inject<ProductRepository>()

    private val scope = componentCoroutineScope()

    private val state = MutableValue(Model(selectedProduct))
    override val model: Value<Model> = state

    override fun handleEvent(event: ProductDetailsEvent) {
        when (event) {
            ProductDetailsEvent.Update -> save()
            is ProductDetailsEvent.UpdateTitle -> updateTitle(event.title)
            is ProductDetailsEvent.Delete -> delete()
        }
    }

    private fun updateTitle(title: String) {
        state.update {
            it.copy(
                product = it.product.copy(title = title)
            )
        }
    }

    private fun delete() {
        scope.launch {
            val productId = state.value.product.id
            state.update { it.copy(isLoading = true) }
            productRepository.deleteProduct(productId)
            state.update { it.copy(isLoading = false) }
            onDeleted(productId)
        }
    }

    private fun save() {
        scope.launch {
            val product = state.value.product
            state.update { it.copy(isLoading = true) }
            productRepository.updateProduct(product)
            state.update { it.copy(isLoading = false) }
            onUpdated(product)
        }
    }
}