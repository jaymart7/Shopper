package ui.signup

import com.arkivanov.decompose.value.Value
import model.request.AccountRequest

internal interface SignUpComponent {
    val model: Value<Model>

    data class Model(
        val isLoading: Boolean = false,
        val usernameError: String? = null
    )

    fun handleEvent(event: SignUpEvent)
}

internal sealed class SignUpEvent {
    data class SignUp(val accountRequest: AccountRequest) : SignUpEvent()
}