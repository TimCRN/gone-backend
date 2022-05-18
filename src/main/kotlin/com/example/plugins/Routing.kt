package com.example.plugins

import com.auth0.jwt.JWT
import com.example.data.model.User
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import java.util.*

fun Application.configureRouting() {

    // Starting point for a Ktor app:
    routing {

        authenticate {
            route("/api") {
                get("/test") {
                    val principal = call.principal<JWTPrincipal>()
                    val email = principal!!.payload.getClaim("email").asString()
                    val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                    call.respondText("Hello, $email! Token is expired at $expiresAt ms.")
                }
                get("/nee") {
                    call.respondText("sup")
                }
            }
        }
    }
}