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
}

internal class DefaultProductDetailsComponent(
    componentContext: ComponentContext,
    selectedProduct: Product,
    private val onUpdated: (Product) -> Unit
) : ComponentContext by componentContext, ProductDetailsComponent, KoinComponent {

    private val productRepository: ProductRepository by inject<ProductRepository>()

    private val scope = componentCoroutineScope()

    private val state = MutableValue(Model(selectedProduct))
    override val model: Value<Model> = state

    override fun handleEvent(event: ProductDetailsEvent) {
        when (event) {
            ProductDetailsEvent.Update -> save()
            is ProductDetailsEvent.UpdateTitle -> updateTitle(event.title)
        }
    }

    private fun updateTitle(title: String) {
        state.update {
            it.copy(
                product = it.product.copy(title = title)
            )
        }
    }

    private fun save() {
        scope.launch {
            state.update { it.copy(isLoading = true) }
            productRepository.updateProduct(state.value.product)
            state.update { it.copy(isLoading = false) }
            onUpdated(state.value.product)
        }
    }
}