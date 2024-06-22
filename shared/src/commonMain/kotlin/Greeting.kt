import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class Greeting {

    private val client = HttpClient()
    private val platform = getPlatform()

    suspend fun greet(): String {
        val response = client.get("http://127.0.0.1:8080")
        return response.bodyAsText()
    }
}