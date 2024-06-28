package component

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import component.HomeComponent.Model
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import model.presentation.Account
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repository.AccountRepository
import util.ViewState
import kotlin.coroutines.CoroutineContext

interface HomeComponent {
    val model: Value<Model>

    data class Model(
        val accountState: ViewState<Account> = ViewState.Loading
    )

    fun logout()

    fun refreshAccount()
}

internal class DefaultHomeComponent(
    private val onFinished: () -> Unit,
    context: CoroutineContext = Dispatchers.Main.immediate
) : HomeComponent, KoinComponent {

    private val scope = CoroutineScope(context + SupervisorJob())

    private val accountRepository by inject<AccountRepository>()

    private val state = MutableValue(Model())
    override val model: Value<Model> = state

    init {
        fetchAccount()
    }

    private fun fetchAccount() {
        scope.launch {
            try {
                state.update { it.copy(accountState = ViewState.Loading) }
                val account = accountRepository.getAccount()
                state.update { it.copy(accountState = ViewState.Success(account)) }
            } catch (e: Exception) {
                println(e)
                state.update { it.copy(accountState = ViewState.Error(e)) }
            }
        }
    }

    override fun refreshAccount() {
        fetchAccount()
    }

    override fun logout() {
        accountRepository.logout()
        onFinished()
    }
}