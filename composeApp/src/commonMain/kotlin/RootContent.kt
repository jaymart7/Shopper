import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import component.RootComponent
import component.RootComponent.Child

@Composable
fun RootContent(
    component: RootComponent,
    modifier: Modifier = Modifier,
) {
    MaterialTheme {
        Surface(modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.systemBars)) {
            Children(
                stack = component.stack,
                modifier = Modifier.fillMaxSize(),
                animation = stackAnimation(fade() + scale())
            ) {
                Column {
                    TopAppBar(
                        onBack = component::onBackClicked,
                        hasBackStack = component.hasBackStack
                    )
                    when (val instance = it.instance) {
                        is Child.Login -> LoginContent(component = instance.component)
                        is Child.Home -> HomeContent(component = instance.component)
                        is Child.ProductDetails -> ProductDetailsContent(component = instance.component)
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
    onBack: () -> Unit,
    hasBackStack: Boolean,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = { Text("title") },
        navigationIcon = {
            if (hasBackStack) {
                IconButton(
                    onClick = onBack,
                    content = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Menu"
                        )
                    }
                )
            }
        },
    )
}