package ui.productdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import common.FullScreenLoading

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
    FullScreenLoading(model.isLoading)

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

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            TextButton(
                modifier = Modifier.weight(1f),
                onClick = { onEvent(ProductDetailsEvent.OnDelete) },
                enabled = model.isLoading.not(),
                content = {
                    Text("Delete")
                }
            )

            Spacer(Modifier.width(16.dp))

            Button(
                modifier = Modifier.weight(1f),
                onClick = { onEvent(ProductDetailsEvent.OnUpdate) },
                enabled = model.isLoading.not(),
                content = {
                    Text("Update")
                }
            )
        }
    }
}