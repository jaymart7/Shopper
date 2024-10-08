package ui.productdetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import common.DatePickerDialog
import common.LoadingDialog
import common.TimePickerDialog

@Composable
internal fun ProductDetailsContent(
    component: ProductDetailsComponent,
    modifier: Modifier = Modifier
) {
    val model by component.model.subscribeAsState()
    val state = rememberProductDetailsState(model.product)

    ProductDetailsContent(
        onEvent = { component.handleEvent(it) },
        model = model,
        state = state,
        modifier = modifier
    )
}

@Composable
private fun ProductDetailsContent(
    onEvent: (ProductDetailsEvent) -> Unit,
    model: ProductDetailsComponent.Model,
    state: ProductDetailsState,
    modifier: Modifier = Modifier
) {
    when {
        model.isLoading -> LoadingDialog()
        state.isDialogConfirmVisible -> ConfirmDialog(
            onDismissRequest = { state.isDialogConfirmVisible = false },
            onConfirm = {
                state.isDialogConfirmVisible = false
                onEvent(ProductDetailsEvent.OnDelete)
            }
        )

        state.isTimePickerDialogShown -> {
            TimePickerDialog(
                selectedTime = state.time,
                onConfirm = {
                    state.time = it
                    state.isTimePickerDialogShown = false
                },
                onDismiss = { state.isTimePickerDialogShown = false }
            )
        }

        state.isDatePickerDialogShown -> {
            DatePickerDialog(
                selectedDate = state.date,
                onDismiss = { state.isDatePickerDialogShown = false },
                onConfirm = {
                    state.date = it
                    state.isDatePickerDialogShown = false
                }
            )
        }
    }

    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.title,
                onValueChange = { state.title = it },
                singleLine = true,
                label = { Text("Product") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        state.isDatePickerDialogShown = true
                    },
                value = state.dateString,
                onValueChange = {},
                readOnly = true,
                enabled = false,
                label = { Text("Date") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        state.isTimePickerDialogShown = true
                    },
                value = state.timeString,
                onValueChange = {},
                readOnly = true,
                enabled = false,
                label = { Text("Time") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
        }

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            TextButton(
                modifier = Modifier.weight(1f),
                onClick = { state.isDialogConfirmVisible = true },
                content = {
                    Text("Delete")
                }
            )

            Spacer(Modifier.width(16.dp))

            Button(
                modifier = Modifier.weight(1f),
                onClick = { onEvent(ProductDetailsEvent.OnUpdate(state.updatedProduct)) },
                enabled = state.isUpdateEnabled,
                content = {
                    Text("Update")
                }
            )
        }
    }
}

@Composable
private fun ConfirmDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        title = { Text("Delete product") },
        text = { Text("Are you sure you want to delete this product?") },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Yes") }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) { Text("No") }
        },
    )
}