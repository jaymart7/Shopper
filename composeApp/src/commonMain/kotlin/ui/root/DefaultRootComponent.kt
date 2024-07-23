package ui.root

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
import model.Product
import org.koin.core.component.KoinComponent
import ui.home.DefaultHomeComponent
import ui.home.HomeComponent
import ui.login.DefaultLoginComponent
import ui.login.LoginComponent
import ui.newproduct.DefaultNewProductComponent
import ui.newproduct.NewProductComponent
import ui.productdetails.DefaultProductDetailsComponent
import ui.productdetails.ProductDetailsComponent
import ui.root.RootComponent.Child
import ui.root.RootComponent.Child.Home
import ui.root.RootComponent.Child.Login
import ui.root.RootComponent.Child.NewProduct
import ui.root.RootComponent.Child.ProductDetails
import ui.root.RootComponent.Config
import ui.root.RootComponent.Model

internal class DefaultRootComponent(
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
            onUpdated = { updatedProduct ->
                onBackClicked()
                val homeComponent = (stack.active.instance as? Home)?.component
                homeComponent?.update(updatedProduct)
                showSnackbar("Updated: ${updatedProduct.title}")
            },
            onDeleted = { deletedProduct ->
                onBackClicked()
                val homeComponent = (stack.active.instance as? Home)?.component
                homeComponent?.delete(deletedProduct.id)
                showSnackbar("Deleted: ${product.title}")
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
}