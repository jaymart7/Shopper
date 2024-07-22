package ui.newproduct

import com.arkivanov.decompose.value.Value

internal interface NewProductComponent {
    val model: Value<Model>

    data class Model(
        val isLoading: Boolean = false
    )

    fun handleEvent(event: NewProductEvent)
}

internal sealed class NewProductEvent {
    data class Add(val title: String) : NewProductEvent()
}