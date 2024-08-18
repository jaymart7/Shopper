package common

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
internal fun LoadingDialog() {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.secondary
        )
    }
}