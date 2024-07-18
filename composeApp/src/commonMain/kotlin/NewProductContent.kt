import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import common.FullScreenLoading
import component.NewProductComponent
import component.NewProductEvent

@Composable
internal fun NewProductContent(
    component: NewProductComponent,
    modifier: Modifier = Modifier
) {
    val model by component.model.subscribeAsState()

    NewProductContent(
        onEvent = { component.handleEvent(it) },
        model = model,
        modifier = modifier
    )
}

@Composable
private fun NewProductContent(
    onEvent: (NewProductEvent) -> Unit,
    model: NewProductComponent.Model,
    modifier: Modifier = Modifier
) {
    FullScreenLoading(model.isLoading)

    Column(modifier = modifier.padding(16.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = model.title,
                onValueChange = { onEvent(NewProductEvent.UpdateTitle(it)) }
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onEvent(NewProductEvent.Add) },
            enabled = model.title.isNotBlank(),
            content = {
                Text("Add")
            }
        )
    }
}