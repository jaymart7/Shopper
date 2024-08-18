package ui.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import model.request.LoginRequest

@Composable
internal fun rememberLoginState(): LoginState {
    return rememberSaveable(
        saver = LoginState.Saver()
    ) {
        LoginState()
    }
}

internal class LoginState {
    var username by mutableStateOf("")
    var password by mutableStateOf("")

    val isLoginEnabled by derivedStateOf {
        username.isNotBlank() && password.isNotBlank()
    }

    internal fun loginRequest() = LoginRequest(
        username = username,
        password = password
    )

    companion object {
        fun Saver(): Saver<LoginState, Any> = listSaver(
            save = {
                listOf(
                    it.username, it.password
                )
            },
            restore = { value ->
                LoginState().apply {
                    username = value[0]
                    password = value[1]
                }
            }
        )
    }
}