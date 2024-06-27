import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.appModule
import androidx.compose.desktop.ui.tooling.preview.Preview
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import component.DefaultRootComponent
import org.koin.core.context.startKoin

fun main() = application {
    startKoin {
        modules(
            appModule()
        )
    }

    val lifecycle = LifecycleRegistry()

    // Always create the root component outside Compose on the UI thread
    val root =
        runOnUiThread {
            DefaultRootComponent(
                componentContext = DefaultComponentContext(lifecycle = lifecycle),
            )
        }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Shopper",
    ) {
        RootContent(root)
    }
}

@Preview
@Composable
private fun AppPreview() {
    LoginScreenPreview()
}