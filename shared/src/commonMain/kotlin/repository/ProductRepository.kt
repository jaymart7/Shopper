package repository

import io.ktor.client.HttpClient
import kotlinx.coroutines.delay
import model.Product

interface ProductRepository {

    suspend fun getProduct(): List<Product>

    suspend fun updateProduct(product: Product)

    suspend fun deleteProduct(id: Int)
}

class ProductRepositoryImpl(
    private val httpClient: HttpClient
) : ProductRepository {

    override suspend fun getProduct(): List<Product> {
        // TODO
        val a = mutableListOf<Product>()
        for (i in 1..100) {
            a.add(Product(i, "pr $i"))
        }

        return a
    }

    override suspend fun updateProduct(product: Product) {
        delay(1000)
        // TODO
    }

    override suspend fun deleteProduct(id: Int) {
        delay(1000)
        //TODO
    }
}