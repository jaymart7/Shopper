import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import common.Home
import common.Login
import common.Screen
import common.ScreenNavigator
import kotlinx.coroutines.CoroutineScope
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
internal fun App() {
    MaterialTheme {
        val scope: CoroutineScope = rememberCoroutineScope()
        val screenNavigator = koinInject<ScreenNavigator> { parametersOf(scope) }
        val login = koinInject<Login> { parametersOf(scope) }
        val home = koinInject<Home> { parametersOf(scope) }

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
            Screen.HOME -> {
                HomeScreen(home)
            }
        }
    }
}