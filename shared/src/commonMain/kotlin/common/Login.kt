package common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import repository.AccountRepository

interface Login {

    val state: StateFlow<State>

    fun login()

    fun onUsernameChanged(value: String)

    fun onPasswordChanged(value: String)

    data class State(
        var username: String = "",
        var password: String = "",
        var loginError: String? = null,
        var isLoginEnabled: Boolean = false,
        var isLoading: Boolean = false
    )
}

class LoginImpl(
    private val accountRepository: AccountRepository,
    private val screenNavigator: ScreenNavigator,
    private val scope: CoroutineScope
) : Login {

    private val _state: MutableStateFlow<Login.State> = MutableStateFlow(Login.State())
    override val state: MutableStateFlow<Login.State> = _state

    override fun login() {
        scope.launch {
            _state.update { it.copy(isLoading = true, loginError = null, isLoginEnabled = false) }
            try {
                accountRepository.login(_state.value.username, _state.value.password)
                screenNavigator.updateScreen(Screen.HOME)
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        loginError = e.message.orEmpty(),
                        isLoading = false,
                        isLoginEnabled = true
                    )
                }
            }
        }
    }

    override fun onUsernameChanged(value: String) {
        _state.update { it.copy(username = value) }
        updateLoginButtonState()
    }

    override fun onPasswordChanged(value: String) {
        _state.update { it.copy(password = value) }
        updateLoginButtonState()
    }

    private fun updateLoginButtonState() {
        _state.update {
            it.copy(
                isLoginEnabled = it.username.isNotBlank() && it.password.isNotBlank()
            )
        }
    }

}