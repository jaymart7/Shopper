package ui.login

import com.arkivanov.decompose.value.Value

internal interface LoginComponent {
    val model: Value<Model>

    fun onUpdateUsername(value: String)

    fun onUpdatePassword(value: String)

    fun login()

    data class Model(
        val username: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val isLoginEnabled: Boolean = false,
        val loginError: String? = null
    )
}