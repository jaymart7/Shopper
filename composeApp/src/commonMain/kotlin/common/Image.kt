package common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

@Composable
internal fun NetworkImage(
    url: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        modifier = modifier,
        model = url,
        contentScale = ContentScale.Crop,
        contentDescription = null,
    )
}