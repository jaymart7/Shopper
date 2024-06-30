import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import component.ProductDetailsComponent

@Composable
internal fun ProductDetailsContent(
    component: ProductDetailsComponent,
    modifier: Modifier = Modifier
) {
    Column {
        Text(component.product.title)
    }
}