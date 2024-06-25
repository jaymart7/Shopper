import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoin {
        modules(appModule())
    }
    ComposeViewport(document.body!!) {
        App()
    }
}