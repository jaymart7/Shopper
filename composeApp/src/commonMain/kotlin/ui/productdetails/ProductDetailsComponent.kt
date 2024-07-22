package ui.productdetails

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import component.componentCoroutineScope
import ui.productdetails.ProductDetailsComponent.Model
import kotlinx.coroutines.launch
import model.Product
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repository.ProductRepository

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
    data object OnUpdate: ProductDetailsEvent()
    data object OnDelete: ProductDetailsEvent()
}

internal sealed class ProductOperation {
    data object Update: ProductOperation()
    data object Delete: ProductOperation()
}

internal class DefaultProductDetailsComponent(
    componentContext: ComponentContext,
    selectedProduct: Product,
    private val onUpdated: (ProductOperation, Product) -> Unit
) : ComponentContext by componentContext, ProductDetailsComponent, KoinComponent {

    private val productRepository: ProductRepository by inject<ProductRepository>()

    private val scope = componentCoroutineScope()

    private val state = MutableValue(Model(selectedProduct))
    override val model: Value<Model> = state

    override fun handleEvent(event: ProductDetailsEvent) {
        when (event) {
            is ProductDetailsEvent.UpdateTitle -> updateTitle(event.title)
            ProductDetailsEvent.OnDelete -> updateProduct(ProductOperation.Delete)
            ProductDetailsEvent.OnUpdate -> updateProduct(ProductOperation.Update)
        }
    }

    private fun updateTitle(title: String) {
        state.update {
            it.copy(
                product = it.product.copy(title = title)
            )
        }
    }

    private fun updateProduct(productOperation: ProductOperation) {
        scope.launch {
            state.update { it.copy(isLoading = true) }
            val product = state.value.product

            when (productOperation) {
                is ProductOperation.Delete -> {
                    productRepository.deleteProduct(product.id)
                }
                is ProductOperation.Update -> {
                    productRepository.updateProduct(product)
                }
            }
            state.update { it.copy(isLoading = false) }
            onUpdated(productOperation, product)
        }
    }
}