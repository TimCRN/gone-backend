package com.example.data.model

import io.ktor.server.auth.*
import kotlinx.serialization.*

@Serializable
data class User(
    val id: Int,
    val email: String,
    val username: String,
    val password: String,
    val profilePicture: String? = null
) : Principal

@Serializable
data class NewUser(
    val id: Int? = null,
    val email: String,
    val username: String,
    val password: String,
    val profilePicture: String? = null
)
