package ui.newproduct

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
import ui.newproduct.NewProductComponent.Model

internal class DefaultNewProductComponent(
    componentContext: ComponentContext,
    private val onShowSnackbar: (String) -> Unit,
    private val onAdded: (Product) -> Unit
) : ComponentContext by componentContext, NewProductComponent, KoinComponent {

    private val productRepository by inject<ProductRepository>()

    private val scope = componentCoroutineScope()

    private val state = MutableValue(Model())
    override val model: Value<Model> = state

    override fun handleEvent(event: NewProductEvent) {
        when (event) {
            is NewProductEvent.Add -> add(event.title)
        }
    }

    private fun add(title: String) {
        scope.launch {
            state.update { it.copy(isLoading = true) }
            try {
                val productId = productRepository.addProduct(title)
                onAdded(
                    Product(
                        id = productId,
                        title = title
                    )
                )
            } catch (e: Exception) {
                onShowSnackbar(e.message.orEmpty())
            } finally {
                state.update { it.copy(isLoading = false) }
            }
        }
    }
}