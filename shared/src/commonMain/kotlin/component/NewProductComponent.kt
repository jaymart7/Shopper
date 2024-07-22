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
        val isLoading: Boolean = false
    )

    fun handleEvent(event: NewProductEvent)
}

sealed class NewProductEvent {
    data class Add(val title: String) : NewProductEvent()
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
            is NewProductEvent.Add -> add(event.title)
        }
    }

    private fun add(title: String) {
        scope.launch {
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