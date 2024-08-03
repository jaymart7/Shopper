package ui.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ui.root.AccountState

@Composable
internal fun AccountContent(
    component: AccountComponent
) {
    val model by component.model.subscribeAsState()

    AccountContent(
        onEvent = component::handleEvent,
        model = model,
    )
}

@Composable
private fun AccountContent(
    onEvent: (AccountEvent) -> Unit,
    model: AccountComponent.Model,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.End
    ) {
        Column(
            modifier = modifier.weight(1f).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            when (val accountState = model.accountState) {
                is AccountState.Error -> {
                    Text(accountState.error.message.orEmpty())
                    Button(
                        onClick = { onEvent(AccountEvent.OnRefresh) },
                        content = { Text("Refresh") }
                    )
                }

                is AccountState.Loading -> CircularProgressIndicator()
                is AccountState.Login -> Button(
                    onClick = { onEvent(AccountEvent.OnNavigateToLogin) },
                    content = { Text("Login") }
                )

                is AccountState.Success -> {
                    Text(accountState.account.name)
                }

                is AccountState.TokenExpired -> {
                    Text(accountState.error.message.orEmpty())
                    Button(
                        onClick = { onEvent(AccountEvent.OnNavigateToLogin) },
                        content = { Text("Logout") }
                    )
                }
            }
        }

        TextButton(
            modifier = Modifier.padding(16.dp),
            onClick = { onEvent(AccountEvent.OnNavigateToLogin) },
            content = { Text("Logout") }
        )
    }
}