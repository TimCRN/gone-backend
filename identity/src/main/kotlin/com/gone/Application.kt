package com.gone

import com.example.services.JwtService
import com.gone.data.DatabaseFactory
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.gone.plugins.*
import com.gone.repository.IdentityRepository

fun main() {
    embeddedServer(Netty, port = 9900, host = "0.0.0.0") {
        DatabaseFactory.init()
        val jwtService = JwtService()
        val identityRepository = IdentityRepository()
        configureRouting(identityRepository,jwtService)
        configureSecurity(identityRepository,jwtService)
        configureHTTP()
    }.start(wait = true)
}
