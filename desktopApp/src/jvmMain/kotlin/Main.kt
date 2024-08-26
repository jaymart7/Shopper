import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import di.appModule
import di.createRootComponent
import org.koin.core.context.startKoin
import ui.root.RootContent

fun main() = application {
    startKoin {
        modules(
            appModule()
        )
    }

    val lifecycle = LifecycleRegistry()

    // Always create the root component outside Compose on the UI thread
    val root = runOnUiThread {
        createRootComponent(DefaultComponentContext(lifecycle = lifecycle))
    }

    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(
            width = 1000.dp,
            height = 800.dp
        ),
        title = "Shopper",
    ) {
        RootContent(root)
    }
}