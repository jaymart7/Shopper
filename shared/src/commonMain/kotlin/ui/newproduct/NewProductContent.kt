package ui.newproduct

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
import common.clickable

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
    when {
        model.isLoading -> LoadingDialog()
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

    Column(modifier = modifier.padding(16.dp)) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.title,
                onValueChange = { state.title = it },
                singleLine = true,
                label = { Text("Product") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                interactionSource = clickable { state.isDatePickerDialogShown = true },
                value = state.dateString,
                onValueChange = {},
                readOnly = true,
                label = { Text("Date") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                interactionSource = clickable { state.isTimePickerDialogShown = true },
                value = state.timeString,
                onValueChange = {},
                readOnly = true,
                label = { Text("Time") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onEvent(NewProductEvent.Add(state.productRequest)) },
            enabled = state.isAddEnabled,
            content = {
                Text("Add")
            }
        )
    }
}