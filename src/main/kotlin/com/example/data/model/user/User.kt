package com.example.data.model.user

import io.ktor.server.auth.*
import kotlinx.serialization.*

@Serializable
data class User(
    val id: Int,
    val email: String,
    val username: String,
    val tag: Int,
    val password: String
)