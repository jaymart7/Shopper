package ui.productdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
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
    var date by mutableStateOf(product.dateTime.date)
    var time by mutableStateOf(product.dateTime.time)

    var isDialogConfirmVisible by mutableStateOf(false)
    var isTimePickerDialogShown by mutableStateOf(false)
    var isDatePickerDialogShown by mutableStateOf(false)

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

    val isUpdateEnabled by derivedStateOf {
        title.isNotBlank() &&
                (title != product.title || date != product.dateTime.date || time != product.dateTime.time)
    }

    val updatedProduct by derivedStateOf {
        Product(
            id = product.id,
            title = title,
            dateTime = LocalDateTime(date, time)
        )
    }

    companion object {
        fun Saver(product: Product): Saver<ProductDetailsState, Any> = listSaver(
            save = {
                listOf(
                    it.title,
                    it.date.toEpochDays(),
                    it.time.toSecondOfDay(),
                    it.isDialogConfirmVisible,
                    it.isDatePickerDialogShown,
                    it.isTimePickerDialogShown
                )
            },
            restore = { value ->
                ProductDetailsState(product).apply {
                    title = value[0] as String
                    date = LocalDate.fromEpochDays(value[1] as Int)
                    time = LocalTime.fromSecondOfDay(value[2] as Int)
                    isDialogConfirmVisible = value[3] as Boolean
                    isDatePickerDialogShown = value[4] as Boolean
                    isTimePickerDialogShown = value[5] as Boolean
                }
            }
        )
    }
}