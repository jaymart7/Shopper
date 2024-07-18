import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.RootComponent
import component.RootComponent.Child

@Composable
fun RootContent(
    component: RootComponent,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val stack by component.stack.subscribeAsState()
    val model by component.model.subscribeAsState()

    // Show snackbar
    model.snackBarMessage?.let {
        LaunchedEffect(it) {
            snackbarHostState.showSnackbar(it)
            component.clearSnackbar()
        }
    }

    MaterialTheme {
        Surface(modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.systemBars)) {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                modifier = modifier,
                topBar = {
                    TopAppBar(
                        onBack = component::onBackClicked,
                        hasBackStack = stack.backStack.isNotEmpty()
                    )
                },
                content = { innerPadding ->
                    Children(
                        stack = stack,
                        modifier = Modifier.padding(innerPadding),
                        animation = stackAnimation(fade() + scale())
                    ) {
                        when (val instance = it.instance) {
                            is Child.Login -> LoginContent(component = instance.component)
                            is Child.Home -> HomeContent(component = instance.component)
                            is Child.ProductDetails -> ProductDetailsContent(instance.component)
                        }
                    }
                }
            )
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
        }
    )
}