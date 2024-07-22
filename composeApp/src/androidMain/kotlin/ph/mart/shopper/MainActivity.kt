package ph.mart.shopper

import ui.home.HomeScreenPreview
import ui.login.LoginScreenPreview
import ui.root.RootContent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.defaultComponentContext
import ui.root.DefaultRootComponent

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