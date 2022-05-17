package com.example.models

import io.ktor.server.auth.*
import java.io.Serializable

data class User(
    val id: Int,
    val email: String,
    val password: String
) : Serializable, Principal
