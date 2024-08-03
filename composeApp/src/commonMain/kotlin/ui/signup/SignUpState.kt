package ui.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import model.request.AccountRequest

@Composable
internal fun rememberSignUpState(): SignUpState {
    return rememberSaveable(
        saver = SignUpState.Saver()
    ) {
        SignUpState()
    }
}

internal class SignUpState {
    var name by mutableStateOf("")
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var passwordVisibility by mutableStateOf(false)

    val isSignUpEnabled by derivedStateOf {
        name.isNotBlank() &&
                username.isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank() &&
                isPasswordMatched
    }

    val isPasswordMatched by derivedStateOf {
        password == confirmPassword || password.isBlank() || confirmPassword.isBlank()
    }

    internal fun accountRequest() = AccountRequest(
        name = name,
        username = username,
        password = password
    )

    companion object {
        fun Saver(): Saver<SignUpState, Any> = listSaver(
            save = {
                listOf(
                    it.name,
                    it.username,
                    it.password,
                    it.confirmPassword
                )
            },
            restore = { value ->
                SignUpState().apply {
                    name = value[0]
                    username = value[1]
                    password = value[2]
                    confirmPassword = value[3]
                }
            }
        )
    }
}