package component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import component.HomeComponent.Model
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import model.Product
import model.presentation.Account
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repository.AccountRepository
import repository.ProductRepository
import util.ViewState

interface HomeComponent {
    val model: Value<Model>

    data class Model(
        val accountState: ViewState<Account> = ViewState.Loading,
        val productsState: ViewState<List<Product>> = ViewState.Loading
    )

    fun handleEvent(event: HomeEvent)

    fun update()
}

sealed class HomeEvent {
    data object Logout : HomeEvent()
    data object RefreshAccount : HomeEvent()
    data object RefreshProduct : HomeEvent()
    data class ProductClick(val product: Product) : HomeEvent()
}

internal class DefaultHomeComponent(
    componentContext: ComponentContext,
    private val onLogout: () -> Unit,
    private val onProductClick: (Product) -> Unit
) : ComponentContext by componentContext, HomeComponent, KoinComponent {

    private val scope = componentCoroutineScope()

    private val accountRepository by inject<AccountRepository>()
    private val productRepository by inject<ProductRepository>()

    private val state = MutableValue(Model())
    override val model: Value<Model> = state

    init {
        fetchAccount()
        fetchProduct()
    }

    override fun handleEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.ProductClick -> onProductClick(event.product)
            is HomeEvent.Logout -> logout()
            is HomeEvent.RefreshAccount -> fetchAccount()
            is HomeEvent.RefreshProduct -> fetchAccount()
        }
    }

    override fun update() {
        // TODO update product
    }

    private fun logout() {
        accountRepository.logout()
        onLogout()
    }

    private fun fetchAccount() {
        scope.launch {
            try {
                state.update { it.copy(accountState = ViewState.Loading) }
                val account = accountRepository.getAccount()
                state.update { it.copy(accountState = ViewState.Success(account)) }
            } catch (e: Exception) {
                Napier.e(e.message.orEmpty(), e)
                state.update { it.copy(accountState = ViewState.Error(e)) }
            }
        }
    }

    private fun fetchProduct() {
        scope.launch {
            try {
                state.update { it.copy(productsState = ViewState.Loading) }
                val products = productRepository.getProduct()
                state.update { it.copy(productsState = ViewState.Success(products)) }
            } catch (e: Exception) {
                println(e)
                state.update { it.copy(productsState = ViewState.Error(e)) }
            }
        }
    }
}