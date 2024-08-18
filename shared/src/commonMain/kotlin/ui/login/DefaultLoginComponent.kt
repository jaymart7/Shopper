package ui.login

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import component.componentCoroutineScope
import kotlinx.coroutines.launch
import model.request.LoginRequest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repository.AccountRepository
import ui.login.LoginComponent.Model

internal class DefaultLoginComponent(
    componentContext: ComponentContext,
    private val onLoggedIn: () -> Unit,
    private val onSignUp: () -> Unit
) : ComponentContext by componentContext, LoginComponent, KoinComponent {

    private val scope = componentCoroutineScope()

    private val accountRepository by inject<AccountRepository>()

    private val state = MutableValue(Model())
    override val model: Value<Model> = state

    override fun handleEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.Login -> login(event.loginRequest)
            is LoginEvent.SignUp -> onSignUp()
        }
    }

    fun login(loginRequest: LoginRequest) {
        scope.launch {
            state.update {
                it.copy(
                    isLoading = true,
                    loginError = null
                )
            }
            try {
                accountRepository.login(loginRequest)
                onLoggedIn()
            } catch (e: Exception) {
                state.update {
                    it.copy(
                        loginError = e.message.orEmpty(),
                        isLoading = false
                    )
                }
            }
        }
    }
}