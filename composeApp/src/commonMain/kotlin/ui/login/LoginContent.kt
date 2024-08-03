package ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
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
        onEvent = { component.handleEvent(it) },
        model = model,
        modifier = modifier
    )
}

@Composable
private fun LoginContent(
    onEvent: (LoginEvent) -> Unit,
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
            text = "Welcome!",
            style = MaterialTheme.typography.bodyLarge
        )

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = model.username,
            enabled = model.isLoading.not(),
            onValueChange = { onEvent(LoginEvent.OnUpdateUserName(it)) },
            label = { Text(stringResource(Res.string.username)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        )

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = model.password,
            enabled = model.isLoading.not(),
            onValueChange = { onEvent(LoginEvent.OnUpdatePassword(it)) },
            label = { Text(stringResource(Res.string.password)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onEvent(LoginEvent.Login) },
            enabled = model.isLoginEnabled,
            content = { Text(stringResource(Res.string.login)) }
        )

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            content = { Text("Sign up") },
            onClick = { onEvent(LoginEvent.SignUp) }
        )

        Text(model.loginError.orEmpty(), color = Color.Red)
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginContent(
        onEvent = {},
        model = LoginComponent.Model()
    )
}