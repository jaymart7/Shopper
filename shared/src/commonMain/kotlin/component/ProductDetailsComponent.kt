package component

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import model.Product
import org.koin.core.component.KoinComponent
import component.ProductDetailsComponent.Model

interface ProductDetailsComponent {
    val model: Value<Model>

    val product: Product

    data class Model(
        val a: String = ""
    )
}

internal class DefaultProductDetailsComponent (
    override val product: Product
) : ProductDetailsComponent, KoinComponent {

    private val state = MutableValue(Model())
    override val model: Value<Model> = state
}