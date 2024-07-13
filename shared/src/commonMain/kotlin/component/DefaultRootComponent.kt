package component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import component.RootComponent.Child
import component.RootComponent.Child.Home
import component.RootComponent.Child.Login
import component.RootComponent.Child.ProductDetails
import kotlinx.serialization.Serializable
import model.Product
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repository.AccountRepository

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    fun onBackClicked()

    sealed class Child {
        class Login(val component: LoginComponent) : Child()
        class Home(val component: HomeComponent) : Child()
        class ProductDetails(
            val component: ProductDetailsComponent
        ) : Child()
    }
}

class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext, KoinComponent {

    private val accountRepository by inject<AccountRepository>()

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = if (accountRepository.hasToken()) {
                Config.Home
            } else {
                Config.Login
            },
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
            back = {
                onBackClicked()
                (stack.active.instance as? Home)?.component?.update()
            }
        )

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
    }
}