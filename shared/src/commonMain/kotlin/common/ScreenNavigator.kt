package common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import repository.AccountRepository

interface ScreenNavigator {

    val state: StateFlow<State>

    fun updateScreen(screen: Screen)

    data class State(
        val screen: Screen = Screen.WELCOME
    )
}

internal class ScreenNavigatorImpl(
    private val accountRepository: AccountRepository,
    val scope: CoroutineScope
) : ScreenNavigator {

    private val _state = MutableStateFlow(ScreenNavigator.State())
    override val state: StateFlow<ScreenNavigator.State> = _state

    init {
        scope.launch {
            if (accountRepository.hasToken()) {
                try {
                    accountRepository.getAccount()
                    _state.update { it.copy(screen = Screen.HOME) }
                } catch (e: Exception) {
                    _state.update { it.copy(screen = Screen.LOGIN) }
                }
            } else {
                _state.update { it.copy(screen = Screen.LOGIN) }
            }
        }
    }

    override fun updateScreen(screen: Screen) {
        _state.update { it.copy(screen = screen) }
    }

}

enum class Screen {
    WELCOME,
    LOGIN,
    HOME
}