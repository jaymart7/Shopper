package ui.root

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import ui.account.AccountContent
import ui.home.HomeContent
import ui.login.LoginContent
import ui.newproduct.NewProductContent
import ui.productdetails.ProductDetailsContent
import ui.root.RootComponent.Child
import ui.signup.SignUpContent
import util.getAsyncImageLoader

@OptIn(ExperimentalCoilApi::class)
@Composable
fun RootContent(
    component: RootComponent,
    modifier: Modifier = Modifier,
) {
    // Coil
    setSingletonImageLoaderFactory { context ->
        getAsyncImageLoader(context)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val stack by component.stack.subscribeAsState()
    val model by component.model.subscribeAsState()

    // Show snackbar
    model.snackBarMessage?.let {
        LaunchedEffect(it) {
            snackbarHostState.showSnackbar(it)
            component.handleEvent(RootEvent.OnClearSnackbar)
        }
    }

    MaterialTheme {
        Surface(modifier = modifier.fillMaxSize().windowInsetsPadding(WindowInsets.systemBars)) {
            RootContent(
                onEvent = component::handleEvent,
                model = model,
                stack = stack,
                snackbarHostState = snackbarHostState,
            )
        }
    }
}

@Composable
private fun RootContent(
    onEvent: (RootEvent) -> Unit,
    model: RootComponent.Model,
    snackbarHostState: SnackbarHostState,
    stack: ChildStack<*, Child>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier,
        topBar = {
            TopAppBar(
                onEvent = onEvent,
                title = stack.active.instance.getTitle(),
                isActionsVisible = stack.active.instance.isActionsVisible(),
                accountState = model.accountState,
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
                    is Child.NewProduct -> NewProductContent(component = instance.component)
                    is Child.Account -> AccountContent(component = instance.component)
                    is Child.SignUp -> SignUpContent(component = instance.component)
                }
            }
        },
        floatingActionButton = {
            if (stack.active.instance.isFabVisible()) {
                FloatingActionButton(
                    onClick = { onEvent(RootEvent.OnFabClick) },
                    content = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add"
                        )
                    }
                )
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
    onEvent: (RootEvent) -> Unit,
    accountState: AccountState,
    title: String,
    isActionsVisible: Boolean,
    hasBackStack: Boolean,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        },
        actions = {
            if (isActionsVisible) {
                ToolbarActionContent(
                    onAccount = { onEvent(RootEvent.OnAccount) },
                    onLogin = { onEvent(RootEvent.OnNavigateToLogin) },
                    accountState = accountState
                )
            }
        },
        navigationIcon = {
            if (hasBackStack) {
                IconButton(
                    onClick = { onEvent(RootEvent.OnBack) },
                    content = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                )
            }
        }
    )
}


@Composable
private fun ToolbarActionContent(
    onAccount: () -> Unit,
    onLogin: () -> Unit,
    accountState: AccountState,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        when (accountState) {
            is AccountState.TokenExpired,
            is AccountState.Error -> {
                IconButton(
                    onClick = onAccount,
                    content = {
                        Icon(
                            imageVector = Icons.Filled.Error,
                            contentDescription = "Account error"
                        )
                    }
                )
            }

            is AccountState.Loading -> LinearProgressIndicator(
                modifier = Modifier
                    .width(32.dp)
                    .height(16.dp)
                    .clickable { onAccount() }
            )

            is AccountState.Success -> {
                TextButton(
                    onClick = onAccount,
                ) {
                    Text(accountState.account.name)
                }
            }

            is AccountState.Login -> TextButton(
                onClick = onLogin,
                content = {
                    Text("Login")
                }
            )
        }
    }
}