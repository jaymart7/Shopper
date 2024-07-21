package component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import component.RootComponent.Child
import component.RootComponent.Child.Home
import component.RootComponent.Child.Login
import component.RootComponent.Child.NewProduct
import component.RootComponent.Child.ProductDetails
import component.RootComponent.Model
import kotlinx.serialization.Serializable
import model.Product
import org.koin.core.component.KoinComponent

interface RootComponent {

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
}

class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext, KoinComponent {

    private val navigation = StackNavigation<Config>()

    private val state = MutableValue(Model())
    override val model: Value<Model> = state

    override val stack: Value<ChildStack<*, Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Home,
            handleBackButton = true,
            childFactory = ::child,
        )

    private fun child(
        config: Config,
        childComponentContext: ComponentContext
    ): Child = when (config) {
        is Config.Login -> Login(loginComponent(childComponentContext))

        is Config.Home -> Home(homeComponent(childComponentContext))

        is Config.ProductDetails -> ProductDetails(
            productComponent(
                componentContext = childComponentContext,
                product = config.product
            )
        )

        is Config.NewProduct -> NewProduct(
            newProductComponent(
                componentContext = childComponentContext
            )
        )
    }

    private fun loginComponent(
        componentContext: ComponentContext
    ): LoginComponent =
        DefaultLoginComponent(
            componentContext = componentContext,
            onLoggedIn = { navigation.replaceAll(Config.Home) },
        )

    private fun homeComponent(componentContext: ComponentContext): HomeComponent =
        DefaultHomeComponent(
            componentContext = componentContext,
            onLogout = { navigation.replaceAll(Config.Login) },
            onProductClick = { navigation.push(Config.ProductDetails(it)) }
        )

    private fun productComponent(
        componentContext: ComponentContext,
        product: Product
    ): ProductDetailsComponent =
        DefaultProductDetailsComponent(
            componentContext = componentContext,
            selectedProduct = product,
            onUpdated = { productOperation, selectedProduct ->
                onBackClicked()
                val homeComponent = (stack.active.instance as? Home)?.component
                when (productOperation) {
                    ProductOperation.Delete -> {
                        homeComponent?.delete(selectedProduct.id)
                        showSnackbar("Deleted: ${product.title}")
                    }

                    ProductOperation.Update -> {
                        homeComponent?.update(selectedProduct)
                        showSnackbar("Updated: ${product.title}")
                    }
                }
            }
        )

    private fun newProductComponent(
        componentContext: ComponentContext
    ): NewProductComponent = DefaultNewProductComponent(
        componentContext = componentContext,
        onAdded = { newProduct ->
            onBackClicked()
            (stack.active.instance as? Home)?.component?.add(newProduct)
            showSnackbar("Added: ${newProduct.title}")
        }
    )

    private fun showSnackbar(message: String) {
        state.update { it.copy(snackBarMessage = message) }
    }

    override fun onFabClicked() {
        navigation.push(Config.NewProduct)
    }

    override fun clearSnackbar() {
        state.update { it.copy(snackBarMessage = null) }
    }

    override fun onBackClicked() {
        navigation.pop()
    }

    @Serializable
    private sealed interface Config {
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

fun Child.isFabVisible(): Boolean {
    return this is Home
}

fun Child.getTitle(): String {
    return when (this) {
        is Home -> "Home"
        is Login -> "Login"
        is NewProduct -> "New Product"
        is ProductDetails -> "Product Details"
    }
}