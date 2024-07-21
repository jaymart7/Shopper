import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import component.DefaultRootComponent
import di.appModule
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoin {
        modules(appModule())
    }

    val lifecycle = LifecycleRegistry()
    val rootComponent =
        DefaultRootComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle)
        )

    lifecycle.resume()
    CanvasBasedWindow("Shopper") { RootContent(rootComponent) }
}