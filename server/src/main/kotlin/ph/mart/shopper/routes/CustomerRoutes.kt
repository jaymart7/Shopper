package ph.mart.shopper.routes

import ph.mart.shopper.model.Customer
import ph.mart.shopper.plugins.customerStorage
import ph.mart.shopper.plugins.json
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString

internal fun Route.customerRouting() {
    route("/customer") {
        get {
            if (customerStorage.isNotEmpty()) {
                call.respond(customerStorage)
            } else {
                call.respondText("No customers found", status = HttpStatusCode.OK)
            }
        }
        post {
            val customer = call.receive<Customer>()
            customerStorage.add(customer)
            call.respondText("Customer stored correctly", status = HttpStatusCode.Created)
        }
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val customer = customerStorage.find { it.id == id }
            if (customer != null) {
                call.respondText(json.encodeToString(customer), status = HttpStatusCode.OK)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (customerStorage.removeIf { it.id == id }) {
                call.respondText("Customer removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
        put("{id?}") {
            val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
            val toUpdate = customerStorage.find { it.id == id }
            val customer = call.receive<Customer>()
            if (toUpdate != null) {
                toUpdate.apply {
                    firstName = customer.firstName
                    lastName = customer.lastName
                    email = customer.email
                }
                call.respondText("Customer updated correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}