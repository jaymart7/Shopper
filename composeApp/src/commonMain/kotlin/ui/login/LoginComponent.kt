package ui.login

import com.arkivanov.decompose.value.Value
import model.request.LoginRequest

internal interface LoginComponent {
    val model: Value<Model>

    data class Model(
        val isLoading: Boolean = false,
        val loginError: String? = null
    )

    fun handleEvent(event: LoginEvent)
}

internal sealed class LoginEvent {
    data class Login(val loginRequest: LoginRequest) : LoginEvent()
    data object SignUp : LoginEvent()
}