package ui.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import model.Product
import model.presentation.Account
import ui.account.AccountComponent
import ui.home.HomeComponent
import ui.login.LoginComponent
import ui.newproduct.NewProductComponent
import ui.productdetails.ProductDetailsComponent
import ui.signup.SignUpComponent

internal interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    val model: Value<Model>

    fun handleEvent(event: RootEvent)

    sealed class Child {
        class Login(val component: LoginComponent) : Child()
        class Home(val component: HomeComponent) : Child()
        class ProductDetails(val component: ProductDetailsComponent) : Child()
        class NewProduct(val component: NewProductComponent) : Child()
        class Account(val component: AccountComponent) : Child()
        class SignUp(val component: SignUpComponent) : Child()
    }

    data class Model(
        val snackBarMessage: String? = null,
        val accountState: AccountState = AccountState.Loading,
        val isFabVisible: Boolean = false
    )

    @Serializable
    sealed interface Config {
        @Serializable
        data object Login : Config

        @Serializable
        data object Home : Config

        @Serializable
        data class ProductDetails(val product: Product) : Config

        @Serializable
        data object NewProduct : Config

        @Serializable
        data object Account : Config

        @Serializable
        data object SignUp : Config
    }
}

internal sealed class RootEvent {
    data object OnBack : RootEvent()
    data object OnAccount : RootEvent()
    data object OnNavigateToLogin : RootEvent()
    data object OnFabClick : RootEvent()
    data object OnClearSnackbar : RootEvent()
}

internal sealed class AccountState {

    data object Loading : AccountState()

    data object Login : AccountState()

    data class Success(val account: Account) : AccountState()

    data class TokenExpired(val error: Throwable) : AccountState()

    data class Error(val error: Throwable) : AccountState()
}