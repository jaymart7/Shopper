package common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.toSize

@Composable
internal fun ButtonWithLoading(
    onClick: () -> Unit,
    isLoading: Boolean,
    enabled: Boolean,
    content: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    var buttonSize by remember { mutableStateOf(DpSize.Zero) }
    val density = LocalDensity.current

    Button(
        modifier = modifier
            .then(
                if (buttonSize != DpSize.Zero) Modifier.size(buttonSize) else Modifier
                    .onSizeChanged { newSize ->
                        if (buttonSize == DpSize.Zero) {
                            buttonSize = with(density) {
                                newSize
                                    .toSize()
                                    .toDpSize()
                            }
                        }
                    }
            ),
        onClick = onClick,
        enabled = enabled,
        content = {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                )
            } else {
                content()
            }
        }
    )
}