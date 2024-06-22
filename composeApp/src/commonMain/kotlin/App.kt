import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import common.Home
import common.HomeImpl
import common.Login
import common.LoginImpl
import common.Screen
import common.ScreenNavigator
import common.ScreenNavigatorImpl
import org.jetbrains.compose.ui.tooling.preview.Preview
import repository.AccountRepositoryImpl
import repository.SessionRepositoryImpl

@Composable
@Preview
fun App() {
    MaterialTheme {
        val scope = rememberCoroutineScope()
        val sessionRepository = SessionRepositoryImpl()
        val accountRepository = AccountRepositoryImpl(
            sessionRepository = sessionRepository
        )
        val screenNavigator = ScreenNavigatorImpl(
            accountRepository = accountRepository,
            scope = scope
        )
        val login = LoginImpl(
            accountRepository = accountRepository,
            screenNavigator = screenNavigator,
            scope = scope
        )
        val home = HomeImpl(
            accountRepository = accountRepository,
            screenNavigator = screenNavigator,
            scope = scope
        )

        MainScreen(
            screenNavigator = screenNavigator,
            login = login,
            home = home
        )
    }
}

@Composable
private fun MainScreen(
    screenNavigator: ScreenNavigator,
    login: Login,
    home: Home,
    modifier: Modifier = Modifier
) {
    val state by screenNavigator.state.collectAsState()

    Column(modifier = modifier) {
        when (state.screen) {
            Screen.WELCOME -> WelcomeScreen(
                onLoginClick = { screenNavigator.updateScreen(Screen.LOGIN) }
            )

            Screen.LOGIN -> LoginScreen(login)
            Screen.HOME -> HomeScreen(home)
        }
    }
}

@Composable
private fun WelcomeScreen(
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        Text("Welcome")

        Button(
            onClick = onLoginClick,
            content = {
                Text("Login")
            }
        )
    }
}

@Composable
private fun LoginScreen(
    login: Login,
    modifier: Modifier = Modifier
) {
    val state by login.state.collectAsState()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        Text("Login")

        TextField(
            value = state.username,
            onValueChange = login::onUsernameChanged,
            label = { Text("Username") }
        )
        TextField(
            value = state.password,
            onValueChange = login::onPasswordChanged,
            label = { Text("Password") }
        )
        Button(
            onClick = login::login,
            enabled = state.isLoginEnabled,
            content = {
                Text("Login")
            }
        )

        Text(state.loginError.orEmpty(), color = Color.Red)
    }
}

@Composable
private fun HomeScreen(
    home: Home,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        Text("Home Screen")
        Button(
            onClick = home::logout,
            content = {
                Text("Logout")
            }
        )
    }
}