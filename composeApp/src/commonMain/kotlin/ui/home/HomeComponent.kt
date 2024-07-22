package ui.home

import com.arkivanov.decompose.value.Value
import model.Product
import model.presentation.Account
import util.ViewState

internal interface HomeComponent {
    val model: Value<Model>

    data class Model(
        val scrollTo: Int? = null,
        val accountState: AccountState = AccountState.Loading,
        val productsState: ViewState<List<Product>> = ViewState.Loading
    )

    fun handleEvent(event: HomeEvent)

    fun update(updatedProduct: Product)

    fun delete(productId: Int)

    fun add(product: Product)
}

internal sealed class AccountState {

    data object Loading : AccountState()

    data object Login : AccountState()

    data object ExpiredToken : AccountState()

    data class Success(val account: Account) : AccountState()

    data class Error(val error: Throwable) : AccountState()
}

internal sealed class HomeEvent {
    data object Logout : HomeEvent()
    data object RefreshAccount : HomeEvent()
    data object RefreshProduct : HomeEvent()
    data object ClearScroll : HomeEvent()
    data class ProductClick(val product: Product) : HomeEvent()
}