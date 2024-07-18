package component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import component.NewProductComponent.Model
import kotlinx.coroutines.launch
import model.Product
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repository.ProductRepository

interface NewProductComponent {
    val model: Value<Model>

    data class Model(
        val isLoading: Boolean = false,
        val title: String = ""
    )

    fun handleEvent(event: NewProductEvent)
}

sealed class NewProductEvent {
    data class UpdateTitle(val title: String) : NewProductEvent()
    data object Add : NewProductEvent()
}

internal class DefaultNewProductComponent(
    componentContext: ComponentContext,
    private val onAdded: (Product) -> Unit
) : ComponentContext by componentContext, NewProductComponent, KoinComponent {

    private val productRepository by inject<ProductRepository>()

    private val scope = componentCoroutineScope()

    private val state = MutableValue(Model())
    override val model: Value<Model> = state

    override fun handleEvent(event: NewProductEvent) {
        when (event) {
            is NewProductEvent.Add -> add()
            is NewProductEvent.UpdateTitle -> updateTitle(event.title)
        }
    }

    private fun updateTitle(title: String) {
        state.update { it.copy(title = title) }
    }

    private fun add() {
        scope.launch {
            val title = state.value.title
            state.update { it.copy(isLoading = true) }
            val productId = productRepository.addProduct(title)
            state.update { it.copy(isLoading = false) }
            onAdded(
                Product(
                    id = productId,
                    title = title
                )
            )
        }
    }

}