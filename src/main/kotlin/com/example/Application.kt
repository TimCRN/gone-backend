package com.example

import com.example.plugins.*
import com.example.data.DatabaseFactory
import com.example.data.DatabaseSeeder
import com.example.repository.ActivityRepository
import com.example.repository.GenderRepository
import com.example.repository.PremiumRepository
import com.example.repository.UserRepository
import com.example.services.JwtService
import com.example.services.hash
import io.github.cdimascio.dotenv.dotenv
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    DatabaseFactory.init()
    val genderRepository = GenderRepository()
    val premiumRepository = PremiumRepository()
    val activityRepository = ActivityRepository()
    val userRepository = UserRepository()
    val jwtService = JwtService()
    val hashFunction = { s:String -> hash(s) }

    runBlocking {
        DatabaseSeeder.seedDatabase(
            genderRepository,
            premiumRepository,
            activityRepository,
            userRepository
        )
    }

    if(dotenv()["DEV_MODE"].toBoolean()){
        install(CORS)
    }

    configureContentNegotiation()
    configureSecurity(userRepository,jwtService)
    configureBasicRouting(userRepository)
    configureAuthRouting(userRepository,jwtService,hashFunction)
}
