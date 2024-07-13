import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import common.ButtonWithLoading
import component.ProductDetailsComponent
import component.ProductDetailsEvent

@Composable
internal fun ProductDetailsContent(
    component: ProductDetailsComponent,
    modifier: Modifier = Modifier
) {
    val model by component.model.subscribeAsState()

    ProductDetailsContent(
        onEvent = { component.handleEvent(it) },
        model = model,
        modifier = modifier
    )
}

@Composable
private fun ProductDetailsContent(
    onEvent: (ProductDetailsEvent) -> Unit,
    model: ProductDetailsComponent.Model,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = model.product.title,
                onValueChange = { onEvent(ProductDetailsEvent.UpdateTitle(it)) }
            )
        }

        ButtonWithLoading(
            modifier = Modifier.padding(16.dp),
            onClick = { onEvent(ProductDetailsEvent.Update) },
            isLoading = model.isLoading,
            enabled = model.isLoading.not(),
            content = {
                Text("Update")
            }
        )
    }
}