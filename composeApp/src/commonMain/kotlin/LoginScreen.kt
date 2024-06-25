import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import common.ButtonWithLoading
import common.Login
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun LoginScreen(
    login: Login,
    modifier: Modifier = Modifier
) {
    val state by login.state.collectAsState()

    LoginScreen(
        onUsernameChanged = login::onUsernameChanged,
        onPasswordChanged = login::onPasswordChanged,
        onLogin = login::login,
        state = state,
        modifier = modifier
    )
}

@Composable
private fun LoginScreen(
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLogin: () -> Unit,
    state: Login.State,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.h6
        )

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.username,
            enabled = state.isLoading.not(),
            onValueChange = onUsernameChanged,
            label = { Text("Username") }
        )

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.password,
            enabled = state.isLoading.not(),
            onValueChange = onPasswordChanged,
            label = { Text("Password") }
        )

        ButtonWithLoading(
            modifier = Modifier.fillMaxWidth(),
            onClick = onLogin,
            enabled = state.isLoginEnabled,
            isLoading = state.isLoading,
            content = { Text("Login") }
        )

        Text(state.loginError.orEmpty(), color = Color.Red)
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onUsernameChanged = {},
        onPasswordChanged = {},
        onLogin = {},
        state = Login.State(isLoading = true)
    )
}