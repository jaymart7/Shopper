package component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import component.HomeComponent.Model
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repository.AccountRepository
import kotlin.coroutines.CoroutineContext

interface HomeComponent {
    val model: Value<Model>

    data class Model(
        val name: String? = null
    )

    fun logout()
}

internal class DefaultHomeComponent(
    private val componentContext: ComponentContext,
    private val onFinished: () -> Unit,
    context: CoroutineContext = Dispatchers.Main.immediate
) : HomeComponent, KoinComponent {

    private val scope = CoroutineScope(context + SupervisorJob())

    private val accountRepository by inject<AccountRepository>()

    init {
        scope.launch {
            val accountResponse = accountRepository.getAccount()
            state.update {
                it.copy(name = accountResponse.name)
            }
        }
    }

    private val state = MutableValue(Model())
    override val model: Value<Model> = state

    override fun logout() {
        accountRepository.logout()
        onFinished()
    }
}