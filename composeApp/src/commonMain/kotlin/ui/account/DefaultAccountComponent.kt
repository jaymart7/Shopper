package ui.account

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import component.componentCoroutineScope
import kotlinx.coroutines.launch
import model.response.ApiError
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repository.AccountRepository
import ui.root.AccountState

internal class DefaultAccountComponent(
    private val onNavigateToLogin: () -> Unit,
    componentContext: ComponentContext,
) : AccountComponent, ComponentContext by componentContext, KoinComponent {

    private val scope = componentCoroutineScope()

    private val accountRepository by inject<AccountRepository>()

    private val state = MutableValue(AccountComponent.Model())
    override val model: Value<AccountComponent.Model> = state

    init {
        fetchAccount()
    }

    override fun handleEvent(event: AccountEvent) {
        when (event) {
            AccountEvent.OnNavigateToLogin -> onNavigateToLogin()
            AccountEvent.OnRefresh -> fetchAccount()
        }
    }

    private fun fetchAccount() {
        if (accountRepository.hasToken().not()) {
            state.update { it.copy(accountState = AccountState.Login) }
            return
        }
        scope.launch {
            try {
                state.update { it.copy(accountState = AccountState.Loading) }
                val account = accountRepository.getAccount()
                state.update { it.copy(accountState = AccountState.Success(account)) }
            } catch (e: Exception) {
                if (e is ApiError && e.code == "401") {
                    state.update { it.copy(accountState = AccountState.TokenExpired(e)) }
                } else {
                    state.update { it.copy(accountState = AccountState.Error(e)) }
                }
            }
        }
    }
}