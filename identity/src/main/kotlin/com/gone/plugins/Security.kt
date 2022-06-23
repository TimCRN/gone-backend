package com.gone.plugins

import io.ktor.server.auth.*
import io.ktor.util.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.services.JwtService
import com.gone.repository.IdentityRepository
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureSecurity(
    identityRepository: IdentityRepository,
    jwtService: JwtService,
) {
    install(Authentication){
        jwt("basic") {
            verifier(jwtService.verifier)
            realm = "basic routes"
            validate { credentials ->
                val email = credentials.payload.getClaim("email").asString()
                val updatedAt = credentials.payload.getClaim("updated_at").asString()
                if (email != "" && updatedAt != "") {
                    val identity = identityRepository.getIdentity(email)
                    identity?.let {
                        if(it.updated_at == updatedAt) {
                            JWTPrincipal(credentials.payload)
                        } else {
                            null
                        }
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