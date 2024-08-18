package ui.productdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import model.Product

@Composable
internal fun rememberProductDetailsState(product: Product): ProductDetailsState {
    return rememberSaveable(
        product,
        saver = ProductDetailsState.Saver(product)
    ) {
        ProductDetailsState(product)
    }
}

internal class ProductDetailsState(product: Product) {
    var title by mutableStateOf(product.title)
    var isDialogConfirmVisible by mutableStateOf(false)
    val isUpdateEnabled by derivedStateOf {
        title.isNotBlank() && title != product.title
    }

    val updatedProduct by derivedStateOf {
        Product(
            id = product.id,
            title = title
        )
    }

    companion object {
        fun Saver(product: Product): Saver<ProductDetailsState, Any> = listSaver(
            save = {
                listOf(
                    it.title,
                    it.isDialogConfirmVisible
                )
            },
            restore = { value ->
                ProductDetailsState(product).apply {
                    title = value[0] as String
                    isDialogConfirmVisible = value[1] as Boolean
                }
            }
        )
    }
}