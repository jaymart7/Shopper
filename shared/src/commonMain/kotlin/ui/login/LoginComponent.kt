package ui.login

import com.arkivanov.decompose.value.Value
import model.request.LoginRequest

interface LoginComponent {
    val model: Value<Model>

    data class Model(
        val isLoading: Boolean = false,
        val loginError: String? = null
    )

    fun handleEvent(event: LoginEvent)
}

sealed class LoginEvent {
    data class Login(val loginRequest: LoginRequest) : LoginEvent()
    data object SignUp : LoginEvent()
}