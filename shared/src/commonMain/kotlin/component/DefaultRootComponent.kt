package component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.popTo
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

    fun onBackClicked(toIndex: Int)

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
        is Config.Login -> Login(loginComponent())

        is Config.Home -> Home(homeComponent())

        is Config.ProductDetails -> ProductDetails(
            component = productComponent(config.product)
        )
    }

    private fun loginComponent(): LoginComponent =
        DefaultLoginComponent(
            onLoggedIn = { navigation.replaceAll(Config.Home) },
        )

    private fun homeComponent(): HomeComponent =
        DefaultHomeComponent(
            onLogout = { navigation.replaceAll(Config.Login) },
            onProductClick = { navigation.push(Config.ProductDetails(it)) }
        )

    private fun productComponent(product: Product): ProductDetailsComponent =
        DefaultProductDetailsComponent(
            product = product
        )

    override fun onBackClicked(toIndex: Int) {
        navigation.popTo(index = toIndex)
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