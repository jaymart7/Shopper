package ui.newproduct

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import model.ProductRequest

@Composable
internal fun rememberNewProductState(): NewProductState {
    return rememberSaveable(
        saver = NewProductState.Saver()
    ) {
        NewProductState()
    }
}

internal class NewProductState {

    val now: Instant = Clock.System.now()
    private val currentDateTime: LocalDateTime =
        now.toLocalDateTime(TimeZone.currentSystemDefault())

    var title by mutableStateOf("")
    var date by mutableStateOf(currentDateTime.date)
    var time by mutableStateOf(currentDateTime.time)

    @OptIn(FormatStringsInDatetimeFormats::class)
    val timeString: String by derivedStateOf {
        LocalTime.Format {
            byUnicodePattern("HH:mm")
        }.format(time)
    }

    @OptIn(FormatStringsInDatetimeFormats::class)
    val dateString: String by derivedStateOf {
        LocalDate.Format {
            byUnicodePattern("MM/dd/yyyy")
        }.format(date)
    }

    var isTimePickerDialogShown by mutableStateOf(false)
    var isDatePickerDialogShown by mutableStateOf(false)

    val isAddEnabled by derivedStateOf {
        title.isNotBlank()
    }

    val productRequest by derivedStateOf {
        ProductRequest(
            title = title,
            dateTime = LocalDateTime(date, time)
        )
    }

    companion object {
        fun Saver(): Saver<NewProductState, Any> = listSaver(
            save = {
                listOf(
                    it.title,
                    it.date.toEpochDays(),
                    it.time.toSecondOfDay(),
                    it.isDatePickerDialogShown,
                    it.isTimePickerDialogShown
                )
            },
            restore = { value ->
                NewProductState().apply {
                    title = value[0] as String
                    date = LocalDate.fromEpochDays(value[1] as Int)
                    time = LocalTime.fromSecondOfDay(value[2] as Int)
                    isDatePickerDialogShown = value[3] as Boolean
                    isTimePickerDialogShown = value[4] as Boolean
                }
            }
        )
    }
}