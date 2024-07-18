package ph.mart.shopper.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import ph.mart.shopper.model.LoginRequest
import ph.mart.shopper.model.request.AccountRequest
import ph.mart.shopper.model.response.ApiError
import ph.mart.shopper.repository.AccountRepository
import java.util.Date

internal fun Route.accountRouting(accountRepository: AccountRepository) {

    val accountNotFound = ApiError(
        code = HttpStatusCode.NotFound.value.toString(),
        message = "Account not found"
    )

    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()

    authenticate("auth-jwt") {
        get("/account") {
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("username").asString()
            val accountResponse = accountRepository.getAccount(username)

            if (accountResponse != null) {
                call.respond(HttpStatusCode.OK, accountResponse)
            } else {
                call.respond(HttpStatusCode.NotFound, accountNotFound)
            }
        }
    }

    route("/login") {
        post {
            val loginRequest = call.receive<LoginRequest>()
            val accountSession = accountRepository.login(loginRequest)

            if (accountSession == null) {
                call.respond(HttpStatusCode.NotFound, accountNotFound)
            } else {
                val token = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("username", loginRequest.username)
                    .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                    .sign(Algorithm.HMAC256(secret))
                call.respond(hashMapOf("token" to token))
            }
        }
    }

    route("/register") {
        post {
            val accountRequest = call.receive<AccountRequest>()

            accountRepository.addAccount(accountRequest)

            call.respond(HttpStatusCode.OK, "Account Successfully created")
        }
    }
}