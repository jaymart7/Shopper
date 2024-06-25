package common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.response.AccountResponse
import repository.AccountRepository

interface Home {

    val state: StateFlow<State>

    data class State(
        var accountResponse: AccountResponse? = null,
        var isLoading: Boolean = false
    )

    fun loadAccount()

    fun logout()
}

internal class HomeImpl(
    private val accountRepository: AccountRepository,
    private val screenNavigator: ScreenNavigator,
    private val scope: CoroutineScope
) : Home {

    private val _state: MutableStateFlow<Home.State> = MutableStateFlow(Home.State())
    override val state: MutableStateFlow<Home.State> = _state

    init {
        loadAccount()
    }

    override fun loadAccount() {
        scope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val accountResponse = accountRepository.getAccount()
                _state.update {
                    it.copy(accountResponse = accountResponse, isLoading = false)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    override fun logout() {
        accountRepository.logout()
        screenNavigator.updateScreen(Screen.LOGIN)
    }
}