package component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import component.LoginComponent.Model
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repository.AccountRepository

interface LoginComponent {
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

internal class DefaultLoginComponent(
    componentContext: ComponentContext,
    private val onLoggedIn: () -> Unit
) : ComponentContext by componentContext, LoginComponent, KoinComponent {

    private val scope = componentCoroutineScope()

    private val accountRepository by inject<AccountRepository>()

    private val state = MutableValue(Model())
    override val model: Value<Model> = state

    override fun onUpdateUsername(value: String) {

        state.update { it.copy(username = value) }
        updateLoginButtonState()
    }

    override fun onUpdatePassword(value: String) {
        state.update { it.copy(password = value) }
        updateLoginButtonState()
    }

    private fun updateLoginButtonState() {
        state.update {
            it.copy(
                isLoginEnabled = it.username.isNotBlank() && it.password.isNotBlank()
            )
        }
    }

    override fun login() {
        scope.launch {
            state.update {
                it.copy(
                    isLoading = true,
                    loginError = null,
                    isLoginEnabled = false
                )
            }
            try {
                accountRepository.login(model.value.username, model.value.password)
                onLoggedIn()
            } catch (e: Exception) {
                state.update {
                    it.copy(
                        loginError = e.message.orEmpty(),
                        isLoading = false,
                        isLoginEnabled = true
                    )
                }
            }
        }
    }
}