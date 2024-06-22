package ph.mart.shopper.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import ph.mart.shopper.model.LoginRequest
import ph.mart.shopper.model.request.AccountRequest
import ph.mart.shopper.repository.AccountRepository
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

internal fun Route.accountRouting(accountRepository: AccountRepository) {

    val secret = environment?.config?.property("jwt.secret")?.getString()
    val issuer = environment?.config?.property("jwt.issuer")?.getString()
    val audience = environment?.config?.property("jwt.audience")?.getString()

    authenticate("auth-jwt") {
        get("/account") {
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("username").asString()
            val accountResponse = accountRepository.getAccount(username)

            if (accountResponse != null) {
                call.respond(HttpStatusCode.OK, accountResponse)
            } else {
                call.respond(HttpStatusCode.NotFound, "Account not found")
            }
        }
    }

    route("/login") {
        post {
            val loginRequest = call.receive<LoginRequest>()
            val accountSession = accountRepository.login(loginRequest)

            if (accountSession == null) {
                call.respondText(
                    "Account not found",
                    status = HttpStatusCode.NotFound
                )
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