import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common.Home

@Composable
internal fun HomeScreen(
    home: Home,
    modifier: Modifier = Modifier
) {
    val state by home.state.collectAsState()
    LaunchedEffect(true) {
        home.loadAccount()
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        Text("Home Screen: ${state.accountResponse?.name}")
        Button(
            onClick = home::logout,
            content = {
                Text("Logout")
            }
        )
    }
}