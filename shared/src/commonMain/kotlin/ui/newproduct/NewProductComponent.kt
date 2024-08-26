package ui.newproduct

import com.arkivanov.decompose.value.Value
import model.ProductRequest

interface NewProductComponent {
    val model: Value<Model>

    data class Model(
        val isLoading: Boolean = false
    )

    fun handleEvent(event: NewProductEvent)
}

sealed class NewProductEvent {
    data class Add(val request: ProductRequest) : NewProductEvent()
}