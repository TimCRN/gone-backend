package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.data.model.User
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureSecurity() {
    val secret = dotenv()["JWT_SECRET"]
    val issuer = "goneBackend"

    install(Authentication){
        jwt {
            verifier(JWT
                .require(Algorithm.HMAC256(secret))
                .withIssuer(issuer)
                .build()
            )
            validate { jwtCredential ->
                if(jwtCredential.payload.getClaim("email").asString() != "") {
                    JWTPrincipal(jwtCredential.payload)
                } else {
                    null
                }
            }
        }
    }

    routing {
        route("/auth"){
            post("/login") {
                val user = call.receive<User>()
                val token = JWT.create()
                    .withIssuer(issuer)
                    .withClaim("email", user.email)
                    .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                    .sign(Algorithm.HMAC256(secret))
                call.respond(hashMapOf("token" to token))
            }
            post("register") {
                val user = call.receive<User>()
                call.respondText("Hello")
            }
        }
    }
}