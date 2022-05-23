package com.example

import com.example.plugins.*
import com.example.repository.DatabaseFactory
import com.example.repository.UserRepository
import com.example.services.JwtService
import com.example.services.hash
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    DatabaseFactory.init()
    val userRepository = UserRepository()
    val jwtService = JwtService()
    val hashFunction = { s:String -> hash(s) }

    if(dotenv()["DEV_MODE"].toBoolean()){
        install(CORS)
    }

    configureContentNegotiation()
    configureSecurity(userRepository,jwtService)
    configureBasicRouting()
    configureAuthRouting(userRepository,jwtService,hashFunction)
}
