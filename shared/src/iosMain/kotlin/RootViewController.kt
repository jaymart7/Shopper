import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import ui.root.RootComponent
import ui.root.RootContent

fun rootViewController(root: RootComponent): UIViewController =
    ComposeUIViewController {
        RootContent(root)
    }