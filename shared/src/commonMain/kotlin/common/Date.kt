package common

import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onConfirm: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    selectedDate: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date
) {
    val selectedDateMillis: Long = selectedDate.toEpochDays().toLong() * 24 * 60 * 60 * 1000

    val state = rememberDatePickerState(
        initialSelectedDateMillis = selectedDateMillis
    )

    Dialog(
        modifier = modifier,
        onDismiss = onDismiss,
        onConfirm = {
            onConfirm(
                Instant
                    .fromEpochMilliseconds(state.selectedDateMillis!!)
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .date
            )
        },
    ) {
        DatePicker(
            state = state,
            showModeToggle = true,
            title = { Text("Select date") }
        )
    }
}