package ui.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import model.Product
import ui.home.HomeComponent
import ui.login.LoginComponent
import ui.newproduct.NewProductComponent
import ui.productdetails.ProductDetailsComponent

internal interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    val model: Value<Model>

    fun onBackClicked()

    fun onFabClicked()

    fun clearSnackbar()

    sealed class Child {
        class Login(val component: LoginComponent) : Child()
        class Home(val component: HomeComponent) : Child()
        class ProductDetails(val component: ProductDetailsComponent) : Child()
        class NewProduct(val component: NewProductComponent) : Child()
    }

    data class Model(
        val snackBarMessage: String? = null,
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
    }
}

