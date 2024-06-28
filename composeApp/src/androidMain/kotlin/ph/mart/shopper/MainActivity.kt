package ph.mart.shopper

import HomeScreenPreview
import LoginScreenPreview
import RootContent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.defaultComponentContext
import component.DefaultRootComponent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = DefaultRootComponent(componentContext = defaultComponentContext())

        setContent {
            RootContent(root)
        }
    }
}

@Preview
@Composable
fun LoginPreview() {
    MaterialTheme {
        Surface {
            LoginScreenPreview()
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    MaterialTheme {
        Surface {
            HomeScreenPreview()
        }
    }
}