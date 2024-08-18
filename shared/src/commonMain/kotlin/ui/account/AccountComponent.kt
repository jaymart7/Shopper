package ui.account

import com.arkivanov.decompose.value.Value
import ui.root.AccountState

interface AccountComponent {

    val model: Value<Model>

    fun handleEvent(event: AccountEvent)

    data class Model(
        val accountState: AccountState = AccountState.Loading,
    )
}

sealed class AccountEvent {
    data object OnRefresh : AccountEvent()
    data object OnNavigateToLogin : AccountEvent()
}