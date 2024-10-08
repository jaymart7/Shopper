package ui.productdetails

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import component.componentCoroutineScope
import kotlinx.coroutines.launch
import model.Product
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repository.ProductRepository
import ui.productdetails.ProductDetailsComponent.Model

internal class DefaultProductDetailsComponent(
    componentContext: ComponentContext,
    selectedProduct: Product,
    private val onShowSnackbar: (String) -> Unit,
    private val onUpdated: (Product) -> Unit,
    private val onDeleted: (Product) -> Unit,
) : ComponentContext by componentContext, ProductDetailsComponent, KoinComponent {

    private val productRepository: ProductRepository by inject<ProductRepository>()

    private val scope = componentCoroutineScope()

    private val state = MutableValue(Model(selectedProduct))
    override val model: Value<Model> = state

    override fun handleEvent(event: ProductDetailsEvent) {
        when (event) {
            is ProductDetailsEvent.OnDelete -> delete()
            is ProductDetailsEvent.OnUpdate -> update(event.product)
        }
    }

    private fun delete() {
        scope.launch {
            try {
                val product = state.value.product
                state.update { it.copy(isLoading = true) }
                productRepository.deleteProduct(product.id)
                onDeleted(product)
            } catch (e: Exception) {
                onShowSnackbar(e.message.orEmpty())
            } finally {
                state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun update(product: Product) {
        scope.launch {
            try {
                state.update { it.copy(isLoading = true) }
                productRepository.updateProduct(product)
                onUpdated(product)
            } catch (e: Exception) {
                onShowSnackbar(e.message.orEmpty())
            } finally {
                state.update { it.copy(isLoading = false) }
            }
        }
    }
}