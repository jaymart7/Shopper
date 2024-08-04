package ui.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import common.LoadingDialog
import common.PasswordTextField

@Composable
internal fun SignUpContent(
    component: SignUpComponent,
    modifier: Modifier = Modifier
) {
    val model by component.model.subscribeAsState()
    val signUpState = rememberSignUpState()

    SignUpContent(
        onEvent = { component.handleEvent(it) },
        model = model,
        state = signUpState,
        modifier = modifier
    )
}

@Composable
private fun SignUpContent(
    onEvent: (SignUpEvent) -> Unit,
    model: SignUpComponent.Model,
    state: SignUpState,
    modifier: Modifier = Modifier
) {
    if (model.isLoading) {
        LoadingDialog()
    }

    Column(modifier = modifier.padding(16.dp)) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.name,
                onValueChange = { state.name = it },
                singleLine = true,
                label = { Text("Name") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.username,
                onValueChange = { state.username = it },
                singleLine = true,
                label = { Text("Username") },
                supportingText = model.usernameError?.let { { Text(it) } },
                isError = model.usernameError != null,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            PasswordTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.password,
                onValueChange = { state.password = it },
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            PasswordTextField(
                supportingText = {
                    if (state.isPasswordMatched.not()) {
                        Text("Password do not match")
                    }
                },
                isError = state.isPasswordMatched.not(),
                modifier = Modifier.fillMaxWidth(),
                value = state.confirmPassword,
                onValueChange = { state.confirmPassword = it },
                label = { Text("Confirm Password") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onEvent(SignUpEvent.SignUp(state.accountRequest())) },
            enabled = state.isSignUpEnabled,
            content = { Text("Create Account") }
        )
    }
}