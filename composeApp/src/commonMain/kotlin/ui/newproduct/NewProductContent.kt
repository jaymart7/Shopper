package ui.newproduct

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
import common.LoadingDialog

@Composable
internal fun NewProductContent(
    component: NewProductComponent,
    modifier: Modifier = Modifier
) {
    val model by component.model.subscribeAsState()
    val newProductState = rememberNewProductState()

    NewProductContent(
        onEvent = { component.handleEvent(it) },
        state = newProductState,
        model = model,
        modifier = modifier
    )
}

@Composable
private fun NewProductContent(
    onEvent: (NewProductEvent) -> Unit,
    state: NewProductState,
    model: NewProductComponent.Model,
    modifier: Modifier = Modifier
) {
    if (model.isLoading) {
        LoadingDialog()
    }

    Column(modifier = modifier.padding(16.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.title,
                onValueChange = { state.title = it }
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onEvent(NewProductEvent.Add(state.title)) },
            enabled = state.isAddEnabled,
            content = {
                Text("Add")
            }
        )
    }
}