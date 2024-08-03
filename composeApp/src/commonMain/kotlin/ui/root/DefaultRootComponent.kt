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
import component.componentCoroutineScope
import kotlinx.coroutines.launch
import model.Product
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repository.AccountRepository
import ui.account.AccountComponent
import ui.account.DefaultAccountComponent
import ui.home.DefaultHomeComponent
import ui.home.HomeComponent
import ui.login.DefaultLoginComponent
import ui.login.LoginComponent
import ui.newproduct.DefaultNewProductComponent
import ui.newproduct.NewProductComponent
import ui.productdetails.DefaultProductDetailsComponent
import ui.productdetails.ProductDetailsComponent
import ui.root.RootComponent.Child
import ui.root.RootComponent.Child.Account
import ui.root.RootComponent.Child.Home
import ui.root.RootComponent.Child.Login
import ui.root.RootComponent.Child.NewProduct
import ui.root.RootComponent.Child.ProductDetails
import ui.root.RootComponent.Child.SignUp
import ui.root.RootComponent.Config
import ui.root.RootComponent.Model
import ui.signup.DefaultSignUpComponent
import ui.signup.SignUpComponent

internal class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext, KoinComponent {

    private val scope = componentCoroutineScope()

    private val accountRepository by inject<AccountRepository>()

    private val navigation = StackNavigation<Config>()

    private val state = MutableValue(Model())
    override val model: Value<Model> = state

    override fun handleEvent(event: RootEvent) {
        when (event) {
            RootEvent.OnAccount -> navigation.push(Config.Account)
            RootEvent.OnBack -> navigation.pop()
            RootEvent.OnClearSnackbar -> state.update { it.copy(snackBarMessage = null) }
            RootEvent.OnFabClick -> navigation.push(Config.NewProduct)
            RootEvent.OnNavigateToLogin -> navigateToLogin()
        }
    }

    init {
        fetchAccount()
    }

    private fun navigateToLogin() {
        accountRepository.logout()
        navigation.push(Config.Login)
    }

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
            newProductComponent(childComponentContext)
        )

        is Config.Account -> Account(
            accountComponent(childComponentContext)
        )

        Config.SignUp -> SignUp(
            signUpComponent(childComponentContext)
        )
    }

    private fun loginComponent(
        componentContext: ComponentContext
    ): LoginComponent =
        DefaultLoginComponent(
            componentContext = componentContext,
            onLoggedIn = {
                navigation.replaceAll(Config.Home)
                fetchAccount()
            },
            onSignUp = { navigation.push(Config.SignUp) }
        )

    private fun homeComponent(componentContext: ComponentContext): HomeComponent =
        DefaultHomeComponent(
            componentContext = componentContext,
            onProductClick = { navigation.push(Config.ProductDetails(it)) }
        )

    private fun productComponent(
        componentContext: ComponentContext,
        product: Product
    ): ProductDetailsComponent =
        DefaultProductDetailsComponent(
            componentContext = componentContext,
            selectedProduct = product,
            onShowSnackbar = ::showSnackbar,
            onUpdated = { updatedProduct ->
                navigation.pop()
                val homeComponent = (stack.active.instance as? Home)?.component
                homeComponent?.update(updatedProduct)
                showSnackbar("Updated: ${updatedProduct.title}")
            },
            onDeleted = { deletedProduct ->
                navigation.pop()
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
            navigation.pop()
            (stack.active.instance as? Home)?.component?.add(newProduct)
            showSnackbar("Added: ${newProduct.title}")
        }
    )

    private fun accountComponent(
        componentContext: ComponentContext
    ): AccountComponent = DefaultAccountComponent(
        componentContext = componentContext,
        onNavigateToLogin = { navigateToLogin() }
    )

    private fun signUpComponent(
        componentContext: ComponentContext
    ): SignUpComponent = DefaultSignUpComponent(
        componentContext = componentContext,
        onSignUpSuccess = {
            navigation.pop()
            showSnackbar("Signed up successfully")
        }
    )

    private fun showSnackbar(message: String) {
        state.update { it.copy(snackBarMessage = message) }
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
                state.update { it.copy(accountState = AccountState.Error(e)) }
            }
        }
    }
}