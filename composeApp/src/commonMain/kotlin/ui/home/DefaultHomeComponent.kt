package ui.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import component.componentCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import model.Product
import model.response.ApiError
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repository.AccountRepository
import repository.ProductRepository
import ui.home.HomeComponent.Model
import util.ViewState

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
            is HomeEvent.ClearScroll -> clearScroll()
            is HomeEvent.RefreshProduct -> fetchAccount()
        }
    }

    private fun clearScroll() {
        state.update { it.copy(scrollTo = null) }
    }

    override fun update(updatedProduct: Product) {
        scope.launch {
            // Find and update product list
            val updatedProducts = getProducts()?.map {
                if (it.id == updatedProduct.id) {
                    updatedProduct
                } else {
                    it
                }
            }

            // Update state with updated products
            updatedProducts?.let { updateProducts(it) }
        }
    }

    override fun delete(productId: Int) {
        scope.launch {
            // Remove from product list
            val updatedProducts = getProducts()?.filter { it.id != productId }
            delay(500) // For animateItemPlacement

            // Update state with updated products
            updatedProducts?.let { updateProducts(it) }
        }
    }

    override fun add(product: Product) {
        scope.launch {
            val updatedProducts = listOf(product) + getProducts().orEmpty()

            // Update state with updated products
            updateProducts(updatedProducts)

            state.update { it.copy(scrollTo = 0) }
        }
    }

    private fun updateProducts(products: List<Product>) {
        state.update {
            it.copy(
                productsState = ViewState.Success(products)
            )
        }
    }

    private fun getProducts(): List<Product>? {
        return when (val products = state.value.productsState) {
            is ViewState.Success -> products.data
            else -> null
        }
    }

    private fun logout() {
        accountRepository.logout()
        onLogout()
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
                val accountState = if ((e as? ApiError)?.code == "401") {
                    AccountState.ExpiredToken
                } else {
                    AccountState.Error(e)
                }
                state.update { it.copy(accountState = accountState) }
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