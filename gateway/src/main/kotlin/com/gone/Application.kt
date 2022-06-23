package com.gone

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.gone.plugins.*

fun main() {
    embeddedServer(Netty, port = 9000, host = "0.0.0.0") {
        configureRouting()
        configureHTTP()
        configureSerialization()
    }.start(wait = true)
}
