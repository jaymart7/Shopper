import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import common.ButtonWithLoading
import component.LoginComponent
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import shopper.composeapp.generated.resources.Res
import shopper.composeapp.generated.resources.login
import shopper.composeapp.generated.resources.password
import shopper.composeapp.generated.resources.username

@Composable
internal fun LoginContent(
    component: LoginComponent,
    modifier: Modifier = Modifier
) {
    val model by component.model.subscribeAsState()

    LoginContent(
        onUpdateUsername = component::onUpdateUsername,
        onUpdatePassword = component::onUpdatePassword,
        onLogin = component::login,
        model = model,
        modifier = modifier
    )
}

@Composable
private fun LoginContent(
    onUpdateUsername: (String) -> Unit,
    onUpdatePassword: (String) -> Unit,
    onLogin: () -> Unit,
    model: LoginComponent.Model,
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
            text = stringResource(Res.string.login),
            style = MaterialTheme.typography.h6
        )

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = model.username,
            enabled = model.isLoading.not(),
            onValueChange = onUpdateUsername,
            label = { Text(stringResource(Res.string.username)) }
        )

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = model.password,
            enabled = model.isLoading.not(),
            onValueChange = onUpdatePassword,
            label = { Text(stringResource(Res.string.password)) }
        )

        ButtonWithLoading(
            modifier = Modifier.fillMaxWidth(),
            onClick = onLogin,
            enabled = model.isLoginEnabled,
            isLoading = model.isLoading,
            content = { Text(stringResource(Res.string.login)) }
        )

        Text(model.loginError.orEmpty(), color = Color.Red)
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginContent(
        onUpdateUsername = {},
        onUpdatePassword = {},
        onLogin = {},
        model = LoginComponent.Model()
    )
}