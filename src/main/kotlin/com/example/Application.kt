package com.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import com.example.repository.DatabaseFactory
import io.ktor.server.application.*

//fun main() {
//    embeddedServer(Netty, port = 8080, watchPaths = listOf("gone-backend"), host = "0.0.0.0") {
//        DatabaseFactory.init()
//        configureSecurity()
//        configureRouting()
//    }.start(wait = true)
//}

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    DatabaseFactory.init()
    configureSecurity()
    configureRouting()
}
