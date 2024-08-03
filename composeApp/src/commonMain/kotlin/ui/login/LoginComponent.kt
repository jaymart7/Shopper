package ui.login

import com.arkivanov.decompose.value.Value

internal interface LoginComponent {
    val model: Value<Model>

    data class Model(
        val username: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val isLoginEnabled: Boolean = false,
        val loginError: String? = null
    )

    fun handleEvent(event: LoginEvent)
}

internal sealed class LoginEvent {
    data class OnUpdateUserName(val value: String) : LoginEvent()
    data class OnUpdatePassword(val value: String) : LoginEvent()
    data object Login : LoginEvent()
    data object SignUp : LoginEvent()
}