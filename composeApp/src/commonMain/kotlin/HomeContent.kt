import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.HomeComponent
import org.jetbrains.compose.ui.tooling.preview.Preview
import util.ViewState

@Composable
internal fun HomeContent(
    component: HomeComponent,
    modifier: Modifier = Modifier
) {
    val model by component.model.subscribeAsState()

    HomeContent(
        onRefresh = component::refreshAccount,
        onLogout = component::logout,
        model = model,
        modifier = modifier
    )
}

@Composable
private fun HomeContent(
    onRefresh: () -> Unit,
    onLogout: () -> Unit,
    model: HomeComponent.Model,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterVertically)
    ) {
        when (val accountState = model.accountState) {
            is ViewState.Error -> Column {
                Text(
                    accountState.error.message.orEmpty(),
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = onRefresh,
                    content = {
                        Text("Refresh")
                    }
                )
            }
            is ViewState.Loading -> CircularProgressIndicator()
            is ViewState.Success -> Text(accountState.data.name)
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))
        Button(
            onClick = onLogout,
            content = {
                Text("Logout")
            }
        )
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
    HomeContent(
        onRefresh = {},
        onLogout = {},
        model = HomeComponent.Model()
    )
}