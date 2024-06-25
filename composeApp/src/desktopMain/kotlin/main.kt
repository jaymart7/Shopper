import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.appModule
import androidx.compose.desktop.ui.tooling.preview.Preview
import org.koin.core.context.startKoin

fun main() = application {
    startKoin {
        modules(appModule())
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Shopper",
    ) {
        App()
    }
}

@Preview
@Composable
private fun AppPreview() {
    LoginScreenPreview()
}