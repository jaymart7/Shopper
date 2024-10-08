package ui.signup

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import component.componentCoroutineScope
import kotlinx.coroutines.launch
import model.request.AccountRequest
import model.response.ApiError
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repository.AccountRepository
import ui.signup.SignUpComponent.Model

internal class DefaultSignUpComponent(
    componentContext: ComponentContext,
    private val onShowSnackbar: (String) -> Unit,
    private val onSignUpSuccess: () -> Unit
) : SignUpComponent, ComponentContext by componentContext, KoinComponent {

    private val accountRepository by inject<AccountRepository>()

    private val scope = componentCoroutineScope()

    private val state = MutableValue(Model())
    override val model: Value<Model> = state

    override fun handleEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.SignUp -> signUp(event.accountRequest)
        }
    }

    private fun signUp(accountRequest: AccountRequest) {
        scope.launch {
            state.update { it.copy(isLoading = true) }
            try {
                accountRepository.signUp(accountRequest)
                onSignUpSuccess()
            } catch (e: Exception) {
                if (e is ApiError && e.code == ERROR_DUPLICATE_USERNAME) {
                    state.update { it.copy(usernameError = e.message) }
                }
                onShowSnackbar(e.message.orEmpty())
            } finally {
                state.update { it.copy(isLoading = false) }
            }
        }
    }

    companion object {
        const val ERROR_DUPLICATE_USERNAME = "USRNM400"
    }
}