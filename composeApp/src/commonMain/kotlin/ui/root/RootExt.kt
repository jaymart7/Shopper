package ui.root

import ui.root.RootComponent.Child
import ui.root.RootComponent.Child.Home
import ui.root.RootComponent.Child.Login
import ui.root.RootComponent.Child.NewProduct
import ui.root.RootComponent.Child.ProductDetails

internal fun Child.isFabVisible(): Boolean {
    return this is Home
}

internal fun Child.getTitle(): String {
    return when (this) {
        is Home -> "Home"
        is Login -> "Login"
        is NewProduct -> "New Product"
        is ProductDetails -> "Product Details"
    }
}