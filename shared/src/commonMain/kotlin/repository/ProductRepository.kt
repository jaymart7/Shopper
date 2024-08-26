package repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import model.Product
import model.ProductRequest

internal interface ProductRepository {

    suspend fun getProduct(): List<Product>

    suspend fun updateProduct(product: Product)

    suspend fun deleteProduct(id: Int)

    suspend fun addProduct(productRequest: ProductRequest): Int
}

internal class ProductRepositoryImpl(
    private val httpClient: HttpClient
) : ProductRepository {

    companion object {
        private const val URL_PRODUCTS = "products"
    }

    override suspend fun getProduct(): List<Product> {
        val response = httpClient.get(URL_PRODUCTS)
        val product = response.body<List<Product>>()
        return product
    }

    override suspend fun updateProduct(product: Product) {
        httpClient.put("$URL_PRODUCTS/${product.id}") {
            setBody(
                ProductRequest(
                    product.title,
                    product.dateTime
                )
            )
        }
    }

    override suspend fun deleteProduct(id: Int) {
        httpClient.delete("$URL_PRODUCTS/$id")
    }

    override suspend fun addProduct(productRequest: ProductRequest): Int {
        val response = httpClient.post(URL_PRODUCTS) {
            setBody(productRequest)
        }
        return response.body<Int>()
    }
}