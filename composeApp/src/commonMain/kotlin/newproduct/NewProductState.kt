package newproduct

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
internal fun rememberNewProductState(): NewProductState {
    return rememberSaveable(
        saver = NewProductState.Saver()
    ) {
        NewProductState()
    }
}

internal class NewProductState {
    var title by mutableStateOf("")
    val isAddEnabled by derivedStateOf {
        title.isNotBlank()
    }

    companion object {
        fun Saver(): Saver<NewProductState, Any> = listSaver(
            save = {
                listOf(
                    it.title
                )
            },
            restore = { value ->
                NewProductState().apply {
                    title = value[0]
                }
            }
        )
    }
}