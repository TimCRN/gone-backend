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
            validate {
                val payload = it.payload
                val email = payload.getClaim("email").asString()
                val user = users.findByEmail(email)
                user
            }
        }
    }
}