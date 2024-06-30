package repository

import io.ktor.client.HttpClient
import model.Product

interface ProductRepository {

    suspend fun getProduct(): List<Product>
}

class ProductRepositoryImpl(
    private val httpClient: HttpClient
) : ProductRepository {

    override suspend fun getProduct(): List<Product> {
        // TODO
        val products = listOf(
            Product(1, "Product 1"),
            Product(2, "Product 2")
        )
        return products
    }
}