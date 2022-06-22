package com.example.plugins

import com.example.repository.UserRepository
import com.example.services.JwtService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity(
    users: UserRepository,
    jwtService: JwtService,
) {
    install(Authentication){
        jwt("basic") {
            verifier(jwtService.verifier)
            realm = "basic routes"
            validate { credentials ->
                val email = credentials.payload.getClaim("email").asString()
                if (email != "") {
                    val user = users.getUserByEmail(email)
                    user?.let {
                        JWTPrincipal(credentials.payload)
                    } ?: run {
                        null
                    }
                } else {
                    null
                }
            }
        }
    }
}